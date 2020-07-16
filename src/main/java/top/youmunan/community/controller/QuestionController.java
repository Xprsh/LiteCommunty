package top.youmunan.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.youmunan.community.dto.CommentDTO;
import top.youmunan.community.dto.QuestionDTO;
import top.youmunan.community.dto.RelatedQuestionDTO;
import top.youmunan.community.service.CommentService;
import top.youmunan.community.service.QuestionService;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id,
                           Model model){
        QuestionDTO question = questionService.getTotalByQuestionId(id);
        if(question == null){
            return "redirect:/";
        }else {

            // 获取评论列表
            List<CommentDTO> commentList = commentService.listByQuestionId(id,1);
            model.addAttribute("comments", commentList);

            questionService.addViewCount(id);
            model.addAttribute("question", question);

            List<RelatedQuestionDTO> relatedQuestionDTOList = questionService.relatedQuestion(id);
            System.out.println("rela"+relatedQuestionDTOList);
            model.addAttribute("relate",relatedQuestionDTOList);
            return "question";
        }
    }
}
