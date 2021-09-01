package com.codetime.blog.controller;

import com.codetime.blog.ArticleRepository;
import com.codetime.blog.CommentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String article(WebRequest webRequest, Model model) {
        var id = webRequest.getParameter("id");
        var article = articleRepository.findById(Long.parseLong(id));
        if(article.isPresent())
        {
            model.addAttribute("article", article.get());
            //TODO find related comments
            return "article";
        }else{
            return "redirect:/error";
        }
    }
}
