package codefun.ws_demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping("/index")
    public String index() {
        log.debug("index");
        return "index";
    }

    @GetMapping("/index_by_name")
    public String indexByName(
            @RequestParam(name = "name", defaultValue = "index") String name) {
        log.debug("index_by_name: name={}", name);
        return name;
    }
}
