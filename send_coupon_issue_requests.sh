#!/bin/bash

API_ENDPOINT="http://localhost:8080/coupons"

# 1,000명의 유저가 총 2번씩 반복하여 요청 보내기
for userId in {1..1000}; do
    for i in {1..2}; do
        # 요청 바디에 해당하는 JSON 데이터 생성
        REQUEST_BODY="{\"userId\": $userId, \"couponEventId\": 1}"

        # curl 명령어로 POST 요청을 보내고 백그라운드에서 실행 (&)
        curl -X POST \
             -H "Content-Type: application/json" \
             -d "$REQUEST_BODY" \
             "$API_ENDPOINT" &

    done
done

# 모든 백그라운드 프로세스가 완료될 때까지 대기
wait

echo "모든 요청이 완료되었습니다."