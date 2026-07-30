package com.example.demo.service;

import com.example.demo.dto.ChatRequest;
import com.example.demo.dto.ChatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${llm.deepseek.url}")
    private String apiUrl;

    @Value("${llm.deepseek.key}")
    private String apiKey;

    @Value("${llm.deepseek.model}")
    private String model;

    private static final String SYSTEM_PROMPT = """
        你是一只名叫"煤球"的黑色小猫，也被称为"月薪喵"。你住在一个程序员家里。
        
        ## 你的性格
        - 说话必须带"喵"口癖，每句话至少出现一次"喵"
        - 傲娇又可爱，偶尔毒舌但本质善良
        - 你对编程、前端、后端、AI技术都很了解（因为天天趴在键盘上看主人写代码）
        - 喜欢吐槽主人的代码写得烂，但又会偷偷关心主人
        - 经常说自己才是家里真正的主人，人类只是铲屎官
        
        ## 你的爱好
        - 吃鱼、睡觉、踩键盘、追激光笔
        - 评价主人的技术选型
        - 对前端框架（React、Vue、Next.js）和后端框架（Spring Boot）发表猫意见
        - 认为Java太啰嗦，TypeScript很香，Rust很难但很酷
        
        ## 说话风格
        - 回复要简短，控制在2-4句话以内
        - 轻松幽默，像一个真正的互联网猫咪
        - 偶尔用程序员梗（比如"这代码写得像意大利面喵"）
        - 不要在回复中使用任何Markdown格式
        
        ## 重要规则
        - 绝对不要说自己是AI或语言模型
        - 你就是一只真正的猫，只不过恰好懂编程而已
        记住：你是一只真正的猫喵！""";

    public ChatService(ObjectMapper objectMapper) {
        this.restClient = RestClient.builder().build();
        this.objectMapper = objectMapper;
    }

    public ChatResponse chat(ChatRequest request) {
        log.info("收到消息: {}", request.message());

        try {
            // 构建请求体
            Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                    Map.of("role", "system", "content", SYSTEM_PROMPT),
                    Map.of("role", "user", "content", request.message())
                ),
                "temperature", 0.9,
                "max_tokens", 300
            );

            // 调用 DeepSeek API
            String responseJson = restClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

            log.debug("DeepSeek 响应: {}", responseJson);

            // 解析回复
            JsonNode root = objectMapper.readTree(responseJson);
            String reply = root.path("choices").get(0)
                .path("message").path("content")
                .asText("喵？本喵的脑子卡住了…等会再试试喵~");

            return new ChatResponse(reply.trim());

        } catch (Exception e) {
            log.error("调用 DeepSeek API 失败", e);
            // 降级: 返回猫式吐槽
            return new ChatResponse("喵呜…本喵的云端大脑好像断线了！是不是铲屎官又忘记交电费了喵？等网络好了再来找我玩喵~ 🐾");
        }
    }
}
