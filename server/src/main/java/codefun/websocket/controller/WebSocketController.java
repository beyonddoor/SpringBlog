package codefun.websocket.controller;

import codefun.websocket.model.Greeting;
import codefun.websocket.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {
    @GetMapping("/index")
    public String index() {
        return "websocket/test";
    }
}
