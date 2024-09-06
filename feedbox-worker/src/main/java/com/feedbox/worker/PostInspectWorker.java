package com.feedbox.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.inspect.port.in.PostInspectUseCase;
import com.feedbox.application.inspect.port.out.InspectedPostMessageProducePort;
import com.feedbox.domain.model.InspectedPost;
import com.feedbox.infrastructure.kafka.common.OperationType;
import com.feedbox.infrastructure.kafka.common.Topic;
import com.feedbox.infrastructure.kafka.mapper.OriginalPostMessageMapper;
import com.feedbox.infrastructure.kafka.message.OriginalPostMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostInspectWorker {

    private final ObjectMapper objectMapper;
    private final PostInspectUseCase postInspectUseCase;
    private final InspectedPostMessageProducePort inspectedPostMessageProducePort;

    /**
     * 원본 Post를 Kafka Consume 해서 검수(Chat-GPT)하고, 검수된 Post를 Kafka Produce 한다.
     */
    @KafkaListener(
            topics = {Topic.ORIGINAL_POST},
            groupId = "feedbox-post-inspect",
            concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        OriginalPostMessage originalPostMessage = objectMapper.readValue(record.value(), OriginalPostMessage.class);
        OperationType operationType = originalPostMessage.getOperationType();
        if (OperationType.CREATE.equals(operationType)) {
            handleCreate(originalPostMessage);
        }
        if (OperationType.UPDATE.equals(operationType)) {
            this.handleUpdate(originalPostMessage);
        }
        if (OperationType.DELETE.equals(operationType)) {
            this.handleDelete(originalPostMessage);
        }
    }

    private void handleCreate(OriginalPostMessage originalPostMessage) {
        InspectedPost inspectedPost = postInspectUseCase.inspectAndGetIfValid(
                OriginalPostMessageMapper.toDomain(originalPostMessage)
        );
        if (inspectedPost == null) return; // 검수 결과가 좋지 않으면, produce 하지 않음
        inspectedPostMessageProducePort.sendCreateMessage(inspectedPost);
    }

    private void handleUpdate(OriginalPostMessage originalPostMessage) {
        InspectedPost inspectedPost = postInspectUseCase.inspectAndGetIfValid(
                OriginalPostMessageMapper.toDomain(originalPostMessage)
        );
        if (inspectedPost == null) {
            // 수정했을 때 검수 결과가 좋지 않으면, 삭제 처리해야 함
            inspectedPostMessageProducePort.sendDeleteMessage(originalPostMessage.getId());
        } else {
            // 검수 결과가 좋아야 produce
            inspectedPostMessageProducePort.sendUpdateMessage(inspectedPost);
        }
    }

    private void handleDelete(OriginalPostMessage originalPostMessage) {
        // DELETE 메시지는 검수가 필요 없으므로 바로 삭제
        inspectedPostMessageProducePort.sendDeleteMessage(originalPostMessage.getId());
    }
}
