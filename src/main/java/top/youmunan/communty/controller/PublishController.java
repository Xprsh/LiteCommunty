package top.youmunan.communty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.communty.mapper.QuestionMapper;
import top.youmunan.communty.mapper.UserMapper;
import top.youmunan.communty.model.Question;
import top.youmunan.communty.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;


    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("detail") String detail,
            @RequestParam("tags") String tags,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {


        model.addAttribute("title", title);
        model.addAttribute("detail", detail);
        model.addAttribute("tags", tags);

        Question question = new Question();
        question.setTitle(title);
        question.setDetail(detail);
        question.setDetail(tags);
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("user");

        if(user == null){
            session.setAttribute("error", "用户未登录");
            return "publish";
        }

        if(title == null || title.equals("")){
            session.setAttribute("error", "标题不能为空");
            return "publish";
        }

        if(detail == null || detail.equals("")){
            session.setAttribute("error", "内容不能为空");
            return "publish";
        }

        if(tags == null || tags.equals("")){
            session.setAttribute("error", "标签不能为空");
            return "publish";
        }

        question.setCreator(user.getId());
        questionMapper.createQuestion(question);
        return "redirect:/";
    }
}
