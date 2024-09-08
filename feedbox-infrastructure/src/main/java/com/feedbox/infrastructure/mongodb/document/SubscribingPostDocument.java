package com.feedbox.infrastructure.mongodb.document;

import com.feedbox.domain.model.post.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "subscribingInboxPosts")
public class SubscribingPostDocument {

    @Id
    private String id; // postId + followerId 조합
    private Long postId; // 컨텐츠 id
    private Long followerId; // 구독자 user id
    private LocalDateTime postCreatedAt; // 컨텐츠 생성 일시
    private LocalDateTime addedAt; // follower 유저의 구독 목록에 반영된 일시
    private boolean read; // 컨텐츠 조회 여부

    public static SubscribingPostDocument of(Post post, Long followerId) {
        return SubscribingPostDocument.builder()
                .id(generateId(post.getId(), followerId))
                .postId(post.getId())
                .followerId(followerId)
                .postCreatedAt(post.getCreatedAt())
                .addedAt(LocalDateTime.now())
                .read(false)
                .build();
    }

    public static String generateId(Long postId, Long followerId) {
        return String.format("%s_%s", postId, followerId);
    }
}
