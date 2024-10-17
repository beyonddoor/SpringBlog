package codefun.websocket.controller;

import codefun.websocket.model.Greeting;
import codefun.websocket.model.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class MessageController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {
        log.debug("Received message: " + message.getName());
        return new Greeting("Hello, " + message.getName() + "!");
    }
}
