package com.feedbox.infrastructure.chatgpt.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbox.application.inspect.port.out.PostInspectPort;
import com.feedbox.domain.model.InspectionResult;
import com.feedbox.domain.model.Post;
import com.feedbox.infrastructure.chatgpt.client.ChatGptClient;
import com.feedbox.infrastructure.chatgpt.model.ChatGptPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostInspectAdapter implements PostInspectPort {

    private final ChatGptClient chatGptClient;
    private final ObjectMapper objectMapper;

    @Override
    public InspectionResult inspect(Post post, String categoryName) {
        ChatGptPolicy policy = buildPolicy();
        String question = buildQuestion(post, categoryName);
        String result = chatGptClient.instructAndAsk(policy, question);

        try {
            return objectMapper.readValue(result, InspectionResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildQuestion(Post post, String categoryName) {
        return String.format(
                "[%s] %s - %s",
                categoryName,
                post.getTitle(),
                post.getContent()
        );
    }

    private ChatGptPolicy buildPolicy() {
        return ChatGptPolicy.builder()
                .instruction("""
                        GPT, 너가 수행해야 할 작업은 'status' 와 'tags' 두 가지 항목을 JSON 형식으로 반환하는 거야.
                        내가 제공할 정보는 '[게시물 카테고리] 게시물 제목 - 게시물 내용' 형식이야.
                        게시물의 제목과 내용이 게시물 카테고리의 의미나 주제와 잘 맞으면 'status' 항목에 'GOOD' 이라는 문자열을 입력해줘.
                        만약 의미나 주제와 관련이 없어 보이면 'status' 항목에 'BAD' 라는 문자열을 입력해줘.
                        또한, 해시태그처럼 최대 5개의 키워드를 추출하여 'tags' 항목에 리스트로 입력해줘.
                        """)
                .exampleContent("""
                        [개발] 카프카와 이벤트 드리븐 아키텍처 - 분산 아키텍처 환경에서 이벤트를 생성하고,
                        발행된 이벤트를 수신자에게 전송하는 구조로 수신자는 그 이벤트를 처리하는 방식으로
                        상호 간 결합도를 낮추기 위해 비동기 방식으로 메시지를 전달하는 패턴이다.
                        """)
                .exampleResult("{\"status\":\"GOOD\",\"tags\":[\"Kafka\", \"Event-Driven Architecture\", \"Asynchronous Messaging\"]}")
                .build();
    }
}
