package websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 STOMP 协议的端点，并指定访问路径
        registry.addEndpoint("/ws_text");
        registry.addEndpoint("/ws").withSockJS(); // 允许使用 SockJS 作为后备方案
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置消息代理
        registry.enableSimpleBroker("/topic"); // 简单消息代理
        registry.setApplicationDestinationPrefixes("/app"); // 应用路径前缀
    }
}