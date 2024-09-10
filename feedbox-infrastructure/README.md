# Kafka 알아보기

### kafka-ui

Docker로 8089포트에 kafka-ui 실행시킬 수 있게 세팅 완료

### 초기 토픽 세팅

브라우저에 localhost:8089 들어간 후, Topics -> Add a Topic 버튼을 이용해 토픽 생성 화면 이동

다음 토픽을 생성

- feedbox.post.original  (파티션 3, Replication Factor 2, Min In Sync Replicas 2)
- feedbox.post.inspected (파티션 3, Replication Factor 2, Min In Sync Replicas 2)
- feedbox.coupon.request (파티션 3, Replication Factor 2, Min In Sync Replicas 2)

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

# Elasticsearch 알아보기

- Elasticsearch는 기본적으로 REST API를 제공한다. 따라서, HTTP Request를 통해 데이터를 주고 받을 수 있다.
- Elastic Kibana의 Dev Tools(http://localhost:5601/app/dev_tools#/console)에 접속하면 좀 더 쉽게 query가 가능하다.

### 한국어 분석기

- ES는 언어별로 분석기를 지원하는데, 한국어는 지원을 안하는 거 같음
- 플러그인으로 한국어 분석기를 사용할 수 있는데, 아래 url에서 nori 라는 분석기 레퍼런스가 있어서 이거 사용
- https://esbook.kimjmin.net/06-text-analysis/6.7-stemming/6.7.2-nori
- 다음 명령어를 통해 nori plugin을 다운로드 한다.

```
bin/elasticsearch-plugin install analysis-nori
```

### index 생성 및 사용
```sh
PUT post
{
  "mappings" : {
    "properties" : {
      "id" : {
        "type" : "long"
      },
      "title" : {
        "type" : "text",
        "analyzer": "nori"
      },
      "content" : {
        "type" : "text",
        "analyzer": "nori"
      },
      "categoryName" : {
        "type" : "keyword"
      },
      "tags" : {
        "type" : "keyword"
      },
      "indexedAt": {
        "type": "date",
        "format": "date_optional_time||epoch_millis"
      }
    }
  },
  "settings" : {
    "index" : {
      "number_of_shards" : 1,
      "number_of_replicas" : 0
    }
  }
}
```

이후, 아래 명령어를 통해 index가 생성되었는지 확인 가능하다.

```sh
GET post
```

검색은 아래와 같이 진행한다.

```sh
GET post/_search
{
  "query": {
    "bool": {
      "should": [
        { "match": { "title": { "query": "검색어" } } },
        { "match": { "content": { "query": "검색어" } } },
        { "term": { "categoryName": "검색어" } },
        { "term": { "tags": "검색어" } }
      ]
    }
  },
  "from": 0,
  "size": 5
}
```

### Document 예시
```json
{
  "_class" : "com.feedbox.infrastructure.elasticsearch.document.PostDocument",
  "id" : 1,
  "title" : "유산소 운동과 심혈관 건강",
  "content" : "유산소 운동은 심장과 혈관 건강을 증진시켜 고혈압과 심장 질환의 위험을 줄여줍니다.",
  "categoryName" : "헬스",
  "tags" : [
    "헬스",
    "유산소 운동",
    "심혈관 건강",
    "고혈압",
    "심장 질환"
  ],
  "indexedAt" : "2024-09-08T18:40:52.490"
}
```

### access log index template

```sh
POST _template/access-log-index-template
{
  "index_patterns": [
    "access-log-*"
  ],
  "aliases": {
    "access-log": {}
  },
  "mappings": {
    "properties": {
      "@timestamp": {
        "type": "date"
      },
      "@version": {
        "type": "keyword"
      },
      "agent": {
        "properties": {
          "ephemeral_id": {
            "type": "keyword"
          },
          "hostname": {
            "type": "keyword"
          },
          "id": {
            "type": "keyword"
          },
          "name": {
            "type": "keyword"
          },
          "type": {
            "type": "keyword"
          },
          "version": {
            "type": "keyword"
          }
        }
      },
      "bytes": {
        "type": "keyword"
      },
      "client_ip": {
        "type": "keyword"
      },
      "duration": {
        "type": "keyword"
      },
      "ecs": {
        "properties": {
          "version": {
            "type": "keyword"
          }
        }
      },
      "host": {
        "properties": {
          "name": {
            "type": "keyword"
          }
        }
      },
      "http_method": {
        "type": "keyword"
      },
      "http_version": {
        "type": "keyword"
      },
      "input": {
        "properties": {
          "type": {
            "type": "keyword"
          }
        }
      },
      "log": {
        "properties": {
          "file": {
            "properties": {
              "path": {
                "type": "keyword"
              }
            }
          },
          "offset": {
            "type": "long"
          }
        }
      },
      "message": {
        "type": "text"
      },
      "port": {
        "type": "keyword"
      },
      "referrer": {
        "type": "text"
      },
      "request_path": {
        "type": "text"
      },
      "response_status": {
        "type": "keyword"
      },
      "timestamp": {
        "type": "date",
        "format": "dd/MMM/yyyy:HH:mm:ss Z"
      },
      "type": {
        "type": "keyword"
      },
      "user_agent": {
        "type": "text"
      }
    }
  }
}
```