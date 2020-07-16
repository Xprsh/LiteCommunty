package top.youmunan.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.community.dto.PageDTO;
import top.youmunan.community.mapper.UserMapper;
import top.youmunan.community.model.User;
import top.youmunan.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserMapper userMapper;



    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action, Model model,
                          @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
                          @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
                          HttpServletRequest request) {
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if(user == null){
                return "index";
            }
            System.out.println(user);
            PageDTO list = questionService.list(user.getAccountId(), page, size);
            model.addAttribute("questions", list);

        }

        return "profile";
    }
}