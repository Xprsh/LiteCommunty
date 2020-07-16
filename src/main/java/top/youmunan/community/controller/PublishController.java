package top.youmunan.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.community.mapper.QuestionMapper;
import top.youmunan.community.model.Question;
import top.youmunan.community.model.User;
import top.youmunan.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;


    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Integer id, Model model) {
        System.out.println("开始测试rest");
        Question question = questionMapper.selectByPrimaryKey(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("detail", question.getDetail());
        model.addAttribute("tags", question.getTags());
        model.addAttribute("questionId", question.getId());
        return "/publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("detail") String detail,
            @RequestParam("tags") String tags,
            @RequestParam(name= "questionId",required = false) Integer questionId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) {

        System.out.println("title = "+ title + ",detail = "+ detail + "tags = "+tags);

        model.addAttribute("title", title);
        model.addAttribute("detail", detail);
        model.addAttribute("tags", tags);

        Question question = new Question();
        question.setTitle(title);
        question.setDetail(detail);
        question.setTags(tags);
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

        question.setCreator(Integer.valueOf(user.getAccountId()));
        question.setId(questionId);
        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
