package top.youmunan.communty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.communty.mapper.UserMapper;
import top.youmunan.communty.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String  hello(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies !=null){
            for (Cookie cookie:cookies) {
                if(cookie.getName().equals("token")){
                    token = cookie.getValue();
                    break;
                }
            }

            User user = userMapper.findByToken(token);
            if(user != null){
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
            }
        }

        return "index";
    }
}
