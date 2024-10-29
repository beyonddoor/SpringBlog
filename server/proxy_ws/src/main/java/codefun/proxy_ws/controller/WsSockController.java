package codefun.proxy_ws.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WsSockController {
    @MessageMapping("/ws")
    @SendTo("/topic/greetings")
    public String handleMessage(String message) {
        return "Hello, " + message + "!";
    }
}
