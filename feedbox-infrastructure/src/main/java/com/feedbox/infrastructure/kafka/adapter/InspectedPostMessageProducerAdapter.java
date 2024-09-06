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
        InspectedPostMessage message = InspectedPostMessageMapper.toMessage(inspectedPost, OperationType.CREATE);
        sendMessage(message);
    }

    @Override
    public void sendUpdateMessage(InspectedPost inspectedPost) {
        InspectedPostMessage message = InspectedPostMessageMapper.toMessage(inspectedPost, OperationType.UPDATE);
        sendMessage(message);
    }

    @Override
    public void sendDeleteMessage(Long postId) {
        InspectedPostMessage message = InspectedPostMessage.builder()
                .id(postId)
                .payload(null)
                .operationType(OperationType.DELETE)
                .build();
        sendMessage(message);
    }

    private void sendMessage(InspectedPostMessage message) {
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
