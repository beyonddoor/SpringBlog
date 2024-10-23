package codefun.ws_demo.config;

import codefun.ws_demo.handler.MyWebSocketHandler;
import codefun.ws_demo.interceptor.MyWebSocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
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