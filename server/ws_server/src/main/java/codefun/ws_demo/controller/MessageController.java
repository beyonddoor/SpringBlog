package codefun.ws_demo.controller;


import codefun.ws_demo.model.Greeting;
import codefun.ws_demo.model.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class MessageController {
    @MessageMapping("/app/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {
        log.debug("Received message: " + message.getName());
        return new Greeting("Hello, " + message.getName() + "!");
    }
}
