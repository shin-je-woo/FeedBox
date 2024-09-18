# FeedBox 서비스

## ✋ 프로젝트 소개

- 컨텐츠 구독서비스입니다.
- 핵심 기능에 집중하기 위해 로그인과 같은 기본 기능은 생략합니다.
- 다른 팀과 협업한다고 가정합니다.

## ✋ 요구사항

- User는 컨텐츠를 발행한다.
- User는 다른 User를 구독할 수 있다. 구독자는 발행된 컨텐츠를 읽을 수 있다.
- 검색기능을 제공한다.
- User는 구독료 할인 Coupon 발급 요청을 할 수 있다.
- 쿠폰은 인당 1개로 제한하며, 전체 수량은 100개이다. 선착순으로 발급된다.

## ✋ 프로젝트 진행을 위한 Case 가정

- 컨텐츠 팀과 유저정보 팀이 있다.
- 컨텐츠는 내가 속한 팀이 관리하고, 유저정보는 다른 팀이 관리한다.
- 유저정보 팀은 유저 정보, 구독 정보, 카테고리 정보를 관리한다.
- 내가 속한 팀은 유저정보 팀의 REST API를 호출해서 유저정보를 가져온다.
  - 유저정보 팀API는 오류가 발생할 수 있다! (1초 지연 후 500Error)

## 🛠️ Skills

- `Java17`
- `Spring Boot`, `JPA`
- `MySQL`, `Redis`, `MongoDB`, `Eliasticsearch`
- `Apache Kafka`
- `Docker`

## ✋ 도식화

![image](https://github.com/user-attachments/assets/0af6ceed-1bff-4228-b843-23f33f33a2c7)

![image](https://github.com/user-attachments/assets/36fa241e-48cd-493b-80f4-c9b8f1219b55)


## ✋ 개발 내역
1) 작성자가 컨텐츠를 발행한다.
  - 컨텐츠는 발행 즉시 Mysql에 데이터가 저장된다.
  - 작성된 콘텐츠는 Kafka에 produce 된다. (토픽 = feedbox.post.original)
  - feedbox.post.original 토픽을 구독하던 Kafka 컨슈머가 원본 컨텐츠 + 유저팀 API 호출결과를 Redis에 캐싱한다.
2) 발행된 컨텐츠를 검수한다.
  - feedbox.post.original 토픽을 구독하던 Kafaka 컨슈머가 원본 컨텐츠를 consume한다.
  - 컨슈머는 메시지를 받아서 유저정보 팀서버에 카테고리 정보API를 요청한다. 
  - 카테고리 정보 + 원본 컨텐츠를 조합하고, Chat-GPT API를 호출해 카테고리, 컨텐츠 제목, 컨텐츠 내용을 검수한다.
  - 검수결과가 성공이면 검수된 컨텐츠를 Kafka에 produce한다. (토픽 = feedbox.post.inspected)
3) 검수완료된 컨텐츠를 구독함에 넣어준다.
  - feedbox.post.inspected 토픽을 구독하던 Kafka 컨슈머가 검수된 컨텐츠를 consume한다.
  - Kafka 메시지 안의 컨텐츠 ID로 유저정보 팀서버에 구독자 목록API를 요청한다.
  - 유저정보팀은 구독자 목록을 반환한다. (Case 가정에 따라 구독자 목록API는 20%확률로 실패한다.)
  - 검수된 컨텐츠, 구독자 정보를 조합해서 구독함(MongoDB)에 저장한다.
4) 구독함에서 컨텐츠 목록을 조회한다.
   - MySQL에서 컨텐츠 원본정보를 가져온다. ([개선] Redis캐싱된 데이터가 있으면 바로 반환한다.)
   - 구독함(MongoDB)에 저장된 검수된 컨텐츠, 구독자 정보를 가져온다.
   - 각 컨텐츠마다 유저정보 팀서버에 유저정보API, 카테고리 정보API를 호출해서 유저정보를 가져온다.
   - [개선] 유저정보 서버 API들은 20%확률로 1초 지연 후 500Error가 발생할 수 있기 때문에 가져온 정보는 Redis에 캐싱해두고 사용한다.
   - 컨텐츠 + 유저정보를 조합해서 구독자에게 목록으로 제공한다.
5) 컨텐츠를 조회한다.
  - 없으면 MySQL에서 컨텐츠 원본정보를 가져온다. ([개선] Redis캐싱된 데이터가 있으면 바로 반환한다.)
  - 각 컨텐츠마다 유저정보 팀서버에 유저정보API, 카테고리 정보API를 호출해서 유저정보를 가져온다.
  - 컨텐츠 + 유저정보를 조합해서 구독자에게 제공한다.
  - [개선] 유저정보 서버 API들은 20%확률로 1초 지연 후 500Error가 발생할 수 있기 때문에 가져온 정보는 Redis에 캐싱해두고 사용한다.
6) 컨텐츠를 검색한다. 
  - feedbox.post.inspected 토픽을 구독하던 Kafka 컨슈머가 검수된 컨텐츠를 consume한다.
  - Chat-GPT가 검수하면서 생성한 해시 태그를 포함해 검색에 필요한 정보를 Elasticsearch에 인덱싱한다.
  - 분석기는 nori를 사용해 한국어를 잘 분석할 수 있도록 한다. 

7) 구독료 할인 쿠폰 발행 이벤트가 있다. 유저는 쿠폰 발행을 요청한다.
  - API서버는 쿠폰이벤트를 Redis에 캐싱하고, Redis를 통해 쿠폰 중복발급 방지 및 남은 쿠폰 수량 체크 로직을 수행한다.
  - API서버는 위 validation이 통과되면 Kafka에 쿠폰발급 메시지를 produce한다. (토픽명: feedbox.coupon.request)
  - 이 과정에서 DB에 접근은 일어나지 않아 DB부하를 줄일 수 있다. 메시지 produce만 하고 바로 응답한다. (성공, 어뷰징, 수량X 등의 응답)
  - 해당 토픽을 구독하던 Kafka Consumer가 0.2초 간격으로 DB 쿠폰 테이블에 insert한다.

## ✋ 궁금증

- 유저정보 API는 실패해도 Consumer Retry 정책으로 2초마다 재시도한다.
  - 재시도 5회가 초과되면 컨슈머는 DLT에 실패한 메시지를 발행한다. 
  - DLT에 발행된 이벤트는 어떻게 처리할까?

- 도메인 로직과 이벤트 발행을 원자적으로 묶는 방법 ?
  - 현재 구현된 상태로는 트랜잭션 커밋 이후에 카프카에 이벤트를 발행하는 방법이다.
  - 만약, 도메인 로직은 커밋되고, 카프카 이벤트 발행에 실패한다면?
  - 원자성이 지켜지지 않고 데이터 정합성이 깨지게 된다.
  - Transactional Outbox Pattern을 적용하거나, 카프카 커넥트의 CDC를 이용하면 해결할 수 있다.
  - Transactional Outbox Pattern을 적용하면 메시지 발행 순서를 지킬 수 있다는 장점도 얻을 수 있다.

- 선착순 쿠폰발급은 요청이 한번에 몰릴 때 DB부하가 생겨 장애로 이어질 수 있다.
  - 게다가, 쿠폰발급이력을 DB에 Write하고 응답하는 것은 유저 사용성에 좋지 않을 수 있다.
  - 이럴 때 Kafka를 이용해서 DB부하를 줄일 수 있다.
    - 만약, 10,000명의 유저가 한번에 몰려 발급 요청을 한다면 DB에 100,00개의 요청이 몰리게 된다.
    - Kafka는 토픽에 있는 데이터를 큐와 같이 순차적으로 가져와 처리하게 된다.
    - 이 과정에서 처리량을 개발자가 정할 수 있다. 따라서, DB에 부하가 한번에 집중되는 것을 막을 수 있다.
  - API서버는 쿠폰잔여갯수가 남아있을 때 Kafka에 produce만 하고 바로 응답하고, 별도의 Consumer가 DB에 저장처리하면 API서버의 응답 시간을 줄일 수 있다.
  - 쿠폰잔여갯수는 분산서버 및 DB부하를 고려해 인메모리 DB인 Redis에서 관리한다.

## 참고 사이트

- [프로젝트에 새로운 아키텍처 적용하기](https://medium.com/naverfinancial/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-%EC%83%88%EB%A1%9C%EC%9A%B4-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0-99d70df6122b)
- [스프링 코드로 이해하는 핵사고날 아키텍처](https://covenant.tistory.com/258)
- [<Architecture> 헥사고날 아키텍처 구조로 변경해보기 예제](https://willbfine.tistory.com/599)
- [[MongoDB] MongoDB란? 특징과 장단점을 예제로 확인해보자.](https://colevelup.tistory.com/45)
- [[ELK] ELK(Elasticsearch, Logstash, Kibana)에 대한 간단한 소개 및 구성 예시](https://mangkyu.tistory.com/282)
- [Spring Data Elasticsearch 설정 및 검색 기능 구현](https://tecoble.techcourse.co.kr/post/2021-10-19-elasticsearch/)
- [쇼핑몰 만들기 프로젝트 - 엘라스틱 서치(elasticsearch)와 스프링부트 연동해보자](https://velog.io/@yeomyaloo/%EC%87%BC%ED%95%91%EB%AA%B0-%EB%A7%8C%EB%93%A4%EA%B8%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1-%EC%84%9C%EC%B9%98elasticsearch%EC%99%80-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%97%B0%EB%8F%99%ED%95%B4%EB%B3%B4%EC%9E%90)
- [nori 한국어 형태소 분석기](https://velog.io/@dm911/nori-%ED%95%9C%EA%B5%AD%EC%96%B4-%ED%98%95%ED%83%9C%EC%86%8C-%EB%B6%84%EC%84%9D%EA%B8%B0)
- [Elastic Search (2) ES 데이터 처리, 검색과 쿼리 - Query DSL](https://velog.io/@hanblueblue/Elastic-Search-2)
- [[식구하자_MSA] Redis+Kafka 선착순 시스템 쿠폰 서비스 구현해보자!](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90MSA-%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%BF%A0%ED%8F%B0-%EC%84%9C%EB%B9%84%EC%8A%A4-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90)
- [아파치 엑세스 로그를 엘라스틱서치에 인덱싱 해보자.](https://taetaetae.github.io/2018/01/25/apache-access-log-to-es/)
- [Circuitbreaker를 사용한 장애 전파 방지](https://oliveyoung.tech/blog/2023-08-31/circuitbreaker-inventory-squad/)
- [개발자 의식의 흐름대로 적용해보는 서킷브레이커](https://techblog.woowahan.com/15694/)
- [트랜잭셔널 아웃박스 패턴의 실제 구현 사례 (29CM)](https://medium.com/@greg.shiny82/%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%94%EB%84%90-%EC%95%84%EC%9B%83%EB%B0%95%EC%8A%A4-%ED%8C%A8%ED%84%B4%EC%9D%98-%EC%8B%A4%EC%A0%9C-%EA%B5%AC%ED%98%84-%EC%82%AC%EB%A1%80-29cm-0f822fc23edb)
- [분산 시스템에서 메시지 안전하게 다루기](https://blog.gangnamunni.com/post/transactional-outbox/)