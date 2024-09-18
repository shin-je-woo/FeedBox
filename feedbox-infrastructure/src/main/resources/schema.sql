CREATE TABLE IF NOT EXISTS post
(
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '콘텐츠 id',
    title       VARCHAR(100) NOT NULL COMMENT '콘텐츠 제목',
    content     TEXT COMMENT '콘텐츠 내용',
    user_id     INT COMMENT '콘텐츠 작성자 user id',
    category_id INT COMMENT '콘텐츠 카테고리 id',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '콘텐츠 생성일시',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '콘텐츠 수정일시',
    deleted_at  TIMESTAMP COMMENT '콘텐츠 삭제일시'
);

CREATE TABLE IF NOT EXISTS coupon_event
(
    id           INT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 이벤트 id',
    display_name VARCHAR(100) NOT NULL COMMENT '쿠폰 디스플레이용 이름',
    expires_at   TIMESTAMP    NOT NULL COMMENT '쿠폰 만료기한',
    issue_limit  BIGINT       NOT NULL COMMENT '쿠폰 발급 제한 갯수'
);

CREATE TABLE IF NOT EXISTS coupon
(
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 id',
    coupon_event_id INT       NOT NULL COMMENT '쿠폰 이벤트 id',
    user_id         INT       NOT NULL COMMENT '쿠폰을 발급받은 user id',
    issued_at       TIMESTAMP NOT NULL COMMENT '쿠폰 발급 일시',
    used_at         TIMESTAMP COMMENT '쿠폰 사용 일시',
    UNIQUE KEY unique_user_id_coupon_event_id (user_id, coupon_event_id),
    FOREIGN KEY (coupon_event_id) REFERENCES coupon_event (id)
);

CREATE TABLE IF NOT EXISTS event_outbox
(
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '이벤트 id',
    outbox_type   ENUM ('POST') COMMENT '이벤트 타입',
    event_payload TEXT COMMENT '이벤트 페이로드(JSON)',
    published     BOOLEAN COMMENT '이벤트 발행 여부',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '이벤트 생성일시',
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '이벤트 수정일시'
);