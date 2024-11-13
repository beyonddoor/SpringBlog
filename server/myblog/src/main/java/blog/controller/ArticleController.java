package blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/article")
public class ArticleController {

    @RequestMapping("/index")
    public ModelAndView index() {
        log.debug("index~~~");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "article");
        modelAndView.setViewName("article/index");
        return modelAndView;
    }
}
