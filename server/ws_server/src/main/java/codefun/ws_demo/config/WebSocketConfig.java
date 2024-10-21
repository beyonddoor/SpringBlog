package codefun.ws_demo.config;

import codefun.ws_demo.handler.MyWebSocketHandler;
import codefun.ws_demo.interceptor.MyWebSocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.debug("registerWebSocketHandlers");
        registry.addHandler(new MyWebSocketHandler(), "/ws_bin")
                .setAllowedOrigins("*")
                .addInterceptors(new MyWebSocketInterceptor());
    }
}