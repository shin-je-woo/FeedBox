package com.feedbox.infrastructure.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 컨텐츠 검색에 필요한 필드를 정의한다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post")
public class PostDocument {

    @Id
    private Long id;
    private String title;
    private String content;
    private String categoryName; // 유저정보 팀에서 제공한 카테고리명
    private List<String> tags; // Chat-GPT가 추출한 태그들
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime indexedAt;
}
