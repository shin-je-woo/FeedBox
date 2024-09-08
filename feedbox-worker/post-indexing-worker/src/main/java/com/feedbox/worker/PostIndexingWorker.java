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
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostIndexingWorker {

    private final ObjectMapper objectMapper;
    private final PostIndexingUseCase postIndexingUseCase;

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
