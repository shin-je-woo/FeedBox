package com.feedbox.infrastructure.kafka.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.post.port.out.InspectedPostMessageProducePort;
import com.feedbox.domain.model.InspectedPost;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.mapper.InspectedPostMessageMapper;
import com.feedbox.infrastructure.kafka.message.InspectedPostMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InspectedPostMessageProducerAdapter implements InspectedPostMessageProducePort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendCreateMessage(InspectedPost inspectedPost) {
        sendMessage(inspectedPost, OperationType.CREATE);
    }

    @Override
    public void sendUpdateMessage(InspectedPost inspectedPost) {
        sendMessage(inspectedPost, OperationType.UPDATE);
    }

    @Override
    public void sendDeleteMessage(InspectedPost inspectedPost) {
        sendMessage(inspectedPost, OperationType.DELETE);
    }

    private void sendMessage(InspectedPost inspectedPost, OperationType operationType) {
        InspectedPostMessage message = InspectedPostMessageMapper.toMessage(inspectedPost, operationType);
        try {
            kafkaTemplate.send(
                    Topic.INSPECTED_POST,
                    String.valueOf(message.getId()),
                    objectMapper.writeValueAsString(message)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
