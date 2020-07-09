package top.youmunan.communty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.communty.dto.PageDTO;
import top.youmunan.communty.dto.QuestionDTO;
import top.youmunan.communty.mapper.QuestionMapper;
import top.youmunan.communty.mapper.UserMapper;
import top.youmunan.communty.model.Question;
import top.youmunan.communty.model.User;
import top.youmunan.communty.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }

            User user = userMapper.findByToken(token);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
            }

            PageDTO questions = questionService.list(page, size);
            model.addAttribute("questions", questions);
        }

        return "index";
    }
}
