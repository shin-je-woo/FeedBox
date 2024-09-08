package com.feedbox.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.search.port.in.PostIndexingUseCase;
import com.feedbox.domain.model.InspectedPost;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.mapper.InspectedPostMessageMapper;
import com.feedbox.infrastructure.kafka.message.InspectedPostMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostIndexingWorker {

    private final ObjectMapper objectMapper;
    private final PostIndexingUseCase postIndexingUseCase;

    /**
     * 검수된 컨텐츠를 Kafka Consume 해서 Elasticsearch 에 인덱싱한다.
     */
    @KafkaListener(
            topics = {Topic.INSPECTED_POST},
            groupId = "feedbox-post-indexing",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        InspectedPostMessage inspectedPostMessage = objectMapper.readValue(record.value(), InspectedPostMessage.class);
        OperationType operationType = inspectedPostMessage.getOperationType();
        if (OperationType.CREATE.equals(operationType)) {
            handleCreate(inspectedPostMessage);
        }
        if (OperationType.UPDATE.equals(operationType)) {
            handleUpdate(inspectedPostMessage);
        }
        if (OperationType.DELETE.equals(operationType)) {
            handleDelete(inspectedPostMessage);
        }
        log.info("컨텐츠 인덱싱 CREATE 완료. OperationType = {}", operationType);
    }

    private void handleCreate(InspectedPostMessage inspectedPostMessage) {
        InspectedPost inspectedPost = InspectedPostMessageMapper.toDomain(inspectedPostMessage);
        postIndexingUseCase.save(inspectedPost);
    }

    private void handleUpdate(InspectedPostMessage inspectedPostMessage) {
        InspectedPost inspectedPost = InspectedPostMessageMapper.toDomain(inspectedPostMessage);
        postIndexingUseCase.delete(inspectedPost.getPost().getId());
        postIndexingUseCase.save(inspectedPost);
    }

    private void handleDelete(InspectedPostMessage inspectedPostMessage) {
        postIndexingUseCase.delete(inspectedPostMessage.getId());
    }
}
