# FeedBox 서비스

## ✋ 프로젝트 소개

- 컨텐츠 발행 및 구독서비스입니다.
- 핵심 기능에 집중하기 위해 로그인과 같은 기본 기능은 생략합니다.
- 다른 팀과 협업한다고 가정합니다.

## ✋ 요구사항

- User는 컨텐츠를 발행한다.
- User는 다른 User를 구독할 수 있다. 구독자는 발행된 컨텐츠를 읽을 수 있다.
- 검색기능을 제공한다.

## ✋ 프로젝트 진행을 위한 Case 가정

- 컨텐츠 팀과 유저정보 팀이 있다.
- 컨텐츠는 내가 속한 팀이 관리하고, 유저정보는 다른 팀이 관리한다.
- 유저정보 팀은 유저 정보, 구독 정보, 카테고리 정보를 관리한다.
- 내가 속한 팀은 유저정보 팀의 REST API를 호출해서 유저정보를 가져온다.
  - 유저정보 팀API는 오류가 발생할 수 있다! (1초 지연 후 500Error)

## ✋ 개발 내역
1) 작성자가 컨텐츠를 발행한다.
  - 컨텐츠는 발행 즉시 Mysql에 데이터가 저장된다.
  - 작성된 콘텐츠는 Kafka에 produce 된다. (토픽 = feedbox.post.original)
  - feedbox.post.original 토픽을 구독하던 Kafka 컨슈머가 원본 컨텐츠를 Redis에 캐싱한다.
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
   - MySQL에서 컨텐츠 원본정보를 가져온다.
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

## ✋ 궁금증

- 유저정보 API는 실패해도 Consumer Retry 정책으로 2초마다 재시도한다.
  - 재시도 5회가 초과되면 컨슈머는 DLT에 실패한 메시지를 발행한다. 
  - DLT에 발행된 이벤트는 어떻게 처리할까?

## 참고 사이트

- [프로젝트에 새로운 아키텍처 적용하기](https://medium.com/naverfinancial/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-%EC%83%88%EB%A1%9C%EC%9A%B4-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0-99d70df6122b)
- [스프링 코드로 이해하는 핵사고날 아키텍처](https://covenant.tistory.com/258)
- [<Architecture> 헥사고날 아키텍처 구조로 변경해보기 예제](https://willbfine.tistory.com/599)
- [[MongoDB] MongoDB란? 특징과 장단점을 예제로 확인해보자.](https://colevelup.tistory.com/45)
- [[ELK] ELK(Elasticsearch, Logstash, Kibana)에 대한 간단한 소개 및 구성 예시](https://mangkyu.tistory.com/282)
- [Spring Data Elasticsearch 설정 및 검색 기능 구현](https://tecoble.techcourse.co.kr/post/2021-10-19-elasticsearch/)
- [쇼핑몰 만들기 프로젝트 - 엘라스틱 서치(elasticsearch)와 스프링부트 연동해보자](https://velog.io/@yeomyaloo/%EC%87%BC%ED%95%91%EB%AA%B0-%EB%A7%8C%EB%93%A4%EA%B8%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%97%98%EB%9D%BC%EC%8A%A4%ED%8B%B1-%EC%84%9C%EC%B9%98elasticsearch%EC%99%80-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%97%B0%EB%8F%99%ED%95%B4%EB%B3%B4%EC%9E%90)
- [nori 한국어 형태소 분석기](https://velog.io/@dm911/nori-%ED%95%9C%EA%B5%AD%EC%96%B4-%ED%98%95%ED%83%9C%EC%86%8C-%EB%B6%84%EC%84%9D%EA%B8%B0)