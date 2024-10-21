package codefun.ws_demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Controller
@Slf4j
public class WsRawController extends BinaryWebSocketHandler {
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        log.debug("Received text message {}", message.getPayload());
//        // Handle text message
//        String payload = message.getPayload();
//        // Process the text data
//        session.sendMessage(new TextMessage("Received text data: " + payload));
//    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Received text message {}", message.getPayload());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.debug("Received pong message {}", message.getPayload().array());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.debug("Received binary message {}", message.getPayload().array());
        // Handle binary message
        byte[] payload = message.getPayload().array();
        // Process the binary data
        session.sendMessage(new BinaryMessage(("Received binary data of length: " + payload.length).getBytes()));
    }
}
