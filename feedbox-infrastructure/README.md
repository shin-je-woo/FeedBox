# Kafka 알아보기

### kafka-ui

Docker로 8089포트에 kafka-ui 실행시킬 수 있게 세팅 완료

### 초기 토픽 세팅

브라우저에 localhost:8089 들어간 후, Topics -> Add a Topic 버튼을 이용해 토픽 생성 화면 이동

다음 토픽을 생성

- feedbox.post.original  (파티션 3, Replication Factor 2, Min In Sync Replicas 2)
- feedbox.post.inspected (파티션 3, Replication Factor 2, Min In Sync Replicas 2)

ReplicationFactor와 Min In Sync Replicas 갯수는 상관 없음

컨슈머는 각각 3개씩이므로 파티션은 3권장

# MongoDB 알아보기

### database 생성 및 사용

```sh
use feedbox
```

아래 명령어를 통해 db가 잘 설정되었는지 확인 가능

```sh
db
```

### collection 생성
```sh
db.createCollection("subscribingInboxPosts")
```

### collection에 index 생성
```sh
db.subscribingInboxPosts.createIndex( { "followerId": -1, "postCreatedAt": -1 } )
db.subscribingInboxPosts.createIndex( { "postId": -1 } )
```

아래 명령어를 통해 현재 인덱스가 잘 생성되었는지 확인 가능

```sh
db.subscribingInboxPosts.getIndexes()
```

### Document 예시
```json
{
    "_id": "12_2",
    "postId": 12,
    "followerId": 2, 
    "postCreatedAt": "2023-12-25T13:22:58.070Z",
    "addedAt": "2023-12-25T13:24:50.010Z",
    "read": false
}
```