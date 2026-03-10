package org.zzu.schoolimsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * ClassName: WebSocketConfig
 * Package: org.zzu.schoolimsystem.config
 * Description:
 *
 * @Author gly
 * @Create 2026/2/11 23:10
 * @Version 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 前端连接的端点，支持跨域
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端订阅路径前缀，例如 /topic/order/1001
        registry.enableSimpleBroker("/topic");
        // 客户端发送消息的前缀
        registry.setApplicationDestinationPrefixes("/app");
    }
}
