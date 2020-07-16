package top.youmunan.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.youmunan.community.dto.CommentCreateDTO;
import top.youmunan.community.dto.CommentDTO;
import top.youmunan.community.dto.ResultDTO;
import top.youmunan.community.model.Comment;
import top.youmunan.community.model.User;
import top.youmunan.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping(value = "/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(2002, "未登录");
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentID());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(Integer.valueOf(user.getAccountId()));
        Object object = commentService.insert(comment);

        return object;
    }

    @ResponseBody
    @GetMapping(value = "/comment/{id}")
    public ResultDTO<List> comments(@PathVariable("id") Integer id){
        List<CommentDTO> list = commentService.listByQuestionId(id, 2);
        return ResultDTO.okOf(list);
    }
}
