package top.youmunan.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.community.dto.HotQuestionDTO;
import top.youmunan.community.dto.PageDTO;
import top.youmunan.community.mapper.QuestionMapper;
import top.youmunan.community.mapper.UserMapper;
import top.youmunan.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
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
                        @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
                        @RequestParam(name = "search",required = false) String search,
                        @RequestParam(name = "tag",required = false) String tag

    ) {

        List<HotQuestionDTO> hotQuestion = questionService.hotQuestion();
        model.addAttribute("hotQuestions",hotQuestion);

        if(search != null){
            PageDTO questions = questionService.searchList(search,1,page,size);
            model.addAttribute("questions", questions);
            model.addAttribute("indexWord","搜索");
            model.addAttribute("keyWord",search);
            return "index";
        }

        if (tag != null){
            PageDTO questions = questionService.searchList(tag,2,page,size);
            model.addAttribute("questions", questions);
            model.addAttribute("indexWord","标签搜索");
            model.addAttribute("keyWord",tag);
            return "index";
        }

        PageDTO questions = questionService.list(page, size);
        model.addAttribute("questions", questions);
        model.addAttribute("indexWord","发现");
        return "index";
    }
}
