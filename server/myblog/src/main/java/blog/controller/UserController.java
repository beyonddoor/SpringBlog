package blog.controller;

import codefun.blog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "article");
        modelAndView.setViewName("article/index");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(@ModelAttribute User user) {
        log.debug("create {}", user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "article");
        modelAndView.setViewName("article/index");
        return modelAndView;
    }
}
