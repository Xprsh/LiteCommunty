package top.youmunan.communty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.youmunan.communty.dto.QuestionDTO;
import top.youmunan.communty.service.QuestionService;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id,
                           Model model){
        QuestionDTO question = questionService.getTotalByQuestionId(id);
        System.out.println(question);
        if(question == null){
            return "redirect:/";
        }else {
            model.addAttribute("question", question);
            return "question";
        }
    }
}
