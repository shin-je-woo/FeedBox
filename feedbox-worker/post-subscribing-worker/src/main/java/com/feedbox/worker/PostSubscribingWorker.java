package com.feedbox.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.subscribing.port.in.SubscribingPostAddToInboxUseCase;
import com.feedbox.application.subscribing.port.in.SubscribingPostRemoveFromInboxUseCase;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.message.InspectedPostMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSubscribingWorker {

    private final ObjectMapper objectMapper;
    private final SubscribingPostAddToInboxUseCase subscribingPostAddToInboxUseCase;
    private final SubscribingPostRemoveFromInboxUseCase subscribingPostRemoveFromInboxUseCase;

    /**
     * 검수된 컨텐츠를 Kafka Consume 해서 MongoDB에 저장한다.
     */
    @KafkaListener(
            topics = {Topic.INSPECTED_POST},
            groupId = "feedbox-post-subscribing",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        InspectedPostMessage inspectedPostMessage = objectMapper.readValue(record.value(), InspectedPostMessage.class);
        OperationType operationType = inspectedPostMessage.getOperationType();
        if (OperationType.CREATE.equals(operationType)) {
            handleCreate(inspectedPostMessage);
        }
        if (OperationType.UPDATE.equals(operationType)) {
            // DO NOTHING
        }
        if (OperationType.DELETE.equals(operationType)) {
            handleDelete(inspectedPostMessage);
        }
    }

    private void handleCreate(InspectedPostMessage inspectedPostMessage) {
        subscribingPostAddToInboxUseCase.saveSubscribingInboxPost(inspectedPostMessage.getPayload().getPost());
    }

    private void handleDelete(InspectedPostMessage inspectedPostMessage) {
        subscribingPostRemoveFromInboxUseCase.removeSubscribingPostFromInbox(inspectedPostMessage.getId());
    }
}
