package com.feedbox.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.post.port.in.PostResolvingUseCase;
import com.feedbox.domain.model.Post;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.mapper.OriginalPostMessageMapper;
import com.feedbox.infrastructure.kafka.message.OriginalPostMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.feedbox.infrastructure.kafka.common.OperationType.*;

@Component
@RequiredArgsConstructor
public class PostCachingWorker {

    private final ObjectMapper objectMapper;
    private final PostResolvingUseCase postResolvingUseCase;

    /**
     * 원본 컨텐츠를 Kafka Consume 해서 Cache(Redis)에 저장한다.
     */
    @KafkaListener(
            topics = {Topic.ORIGINAL_POST},
            groupId = "feedbox-post-caching",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        OriginalPostMessage originalPostMessage = objectMapper.readValue(record.value(), OriginalPostMessage.class);
        OperationType operationType = originalPostMessage.getOperationType();
        if(CREATE.equals(operationType)) {
            handleCreate(originalPostMessage);
        }
        if(UPDATE.equals(operationType)) {
            handleUpdate(originalPostMessage);
        }
        if(DELETE.equals(operationType)) {
            handleDelete(originalPostMessage);
        }
    }

    private void handleCreate(OriginalPostMessage originalPostMessage) {
        Post post = OriginalPostMessageMapper.toDomain(originalPostMessage);
        postResolvingUseCase.resolveAndSaveCache(post);
    }

    private void handleUpdate(OriginalPostMessage originalPostMessage) {
        Post post = OriginalPostMessageMapper.toDomain(originalPostMessage);
        postResolvingUseCase.deleteCachedResolvedPost(post.getId());
        postResolvingUseCase.resolveAndSaveCache(post);
    }

    private void handleDelete(OriginalPostMessage originalPostMessage) {
        postResolvingUseCase.deleteCachedResolvedPost(originalPostMessage.getId());
    }
}
