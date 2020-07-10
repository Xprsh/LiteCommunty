package top.youmunan.communty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.communty.dto.PageDTO;
import top.youmunan.communty.mapper.QuestionMapper;
import top.youmunan.communty.mapper.UserMapper;
import top.youmunan.communty.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String hello(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
                        @RequestParam(name = "size", defaultValue = "5", required = false) Integer size
    ) {


        PageDTO questions = questionService.list(page, size);
        model.addAttribute("questions", questions);

        return "index";
    }
}
