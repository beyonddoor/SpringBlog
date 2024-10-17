package codefun.netty.controller;

import codefun.netty.service.NettyServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/netty")
public class NettyController {

    @Autowired
    private NettyServerService nettyServerService;

    @GetMapping("/test")
    public void test() {
        log.debug("Test");
    }
}
