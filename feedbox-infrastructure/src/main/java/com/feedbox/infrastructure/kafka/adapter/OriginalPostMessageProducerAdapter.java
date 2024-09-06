package com.feedbox.infrastructure.kafka.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.post.port.out.OriginalPostMessageProducePort;
import com.feedbox.domain.model.Post;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.mapper.OriginalPostMessageMapper;
import com.feedbox.infrastructure.kafka.message.OriginalPostMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OriginalPostMessageProducerAdapter implements OriginalPostMessageProducePort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendCreateMessage(Post post) {
        sendMessage(post, OperationType.CREATE);
    }

    @Override
    public void sendUpdateMessage(Post post) {
        sendMessage(post, OperationType.UPDATE);
    }

    @Override
    public void sendDeleteMessage(Post post) {
        sendMessage(post, OperationType.DELETE);
    }

    private void sendMessage(Post post, OperationType operationType) {
        OriginalPostMessage message = OriginalPostMessageMapper.toMessage(post.getId(), post, operationType);
        try {
            kafkaTemplate.send(
                    Topic.ORIGINAL_POST,
                    String.valueOf(message.getId()),
                    objectMapper.writeValueAsString(message)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
