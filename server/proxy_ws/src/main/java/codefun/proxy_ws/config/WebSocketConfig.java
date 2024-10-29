package codefun.proxy_ws.config;

import codefun.proxy_ws.handler.MyWebSocketHandler;
import codefun.proxy_ws.interceptor.MyWebSocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    final MyWebSocketHandler myWebSocketHandler;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.debug("registerWebSocketHandlers");
        registry.addHandler(myWebSocketHandler, "/ws_bin")
                .setAllowedOrigins("*")
                .addInterceptors(new MyWebSocketInterceptor());
    }
}