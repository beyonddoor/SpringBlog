package websocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSocket
public class NewWebSocketConfig implements WebSocketConfigurer {

    private static final Logger log = LoggerFactory.getLogger(NewWebSocketConfig.class);

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ProxyWebSocketHandler(), "/ws_text");
    }

    @Bean
    public ProxyWebSocketHandler proxyWebSocketHandler() {
        return new ProxyWebSocketHandler();
    }

    private static class ProxyWebSocketHandler extends TextWebSocketHandler {

        private static final String BACKEND_HOST = "localhost";
        private static final int BACKEND_PORT = 8889;

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            log.debug("Received message: " + message.getPayload());
            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(BACKEND_HOST, BACKEND_PORT))) {
                socketChannel.configureBlocking(false);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.clear();
                buffer.put(message.getPayload().getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
                buffer.clear();
                int bytesRead;
                while ((bytesRead = socketChannel.read(buffer)) != -1) {
                    if (bytesRead > 0) {
                        buffer.flip();
                        String response = StandardCharsets.UTF_8.decode(buffer).toString();
                        session.sendMessage(new TextMessage(response));
                        buffer.clear();
                    }
                }
            }
            log.debug("Message sent to backend server");
        }
//        @Override
//        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//            log.debug("Received message: " + message.getPayload());
//            try (Socket socket = new Socket(BACKEND_HOST, BACKEND_PORT)) {
//                OutputStream outputStream = socket.getOutputStream();
//                outputStream.write(message.getPayload().getBytes(StandardCharsets.UTF_8));
//                outputStream.flush();
//
//                InputStream inputStream = socket.getInputStream();
//                byte[] buffer = new byte[1024];
//                int read;
//                while ((read = inputStream.read(buffer)) != -1) {
//                    String response = new String(buffer, 0, read, StandardCharsets.UTF_8);
//                    session.sendMessage(new TextMessage(response));
//                }
//            }
//            log.debug("Message sent to backend server");
//        }
    }
}