package top.youmunan.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.youmunan.community.dto.CommentDTO;
import top.youmunan.community.dto.ResultDTO;
import top.youmunan.community.mapper.CommentMapper;
import top.youmunan.community.mapper.QuestionMapper;
import top.youmunan.community.mapper.UserMapper;
import top.youmunan.community.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;




    @Transactional
    public Object insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
            return ResultDTO.errorOf(2002, "未登录");
        }

        if (comment.getContent() == null || comment.getContent().equals("") || !(comment.getType() == 1 || comment.getType() == 2) ){
            return ResultDTO.errorOf(2003, "评论内容错误或请求类型错误");
        }

        if(comment.getType() == 1){
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId().intValue());
            if(question == null){
                return ResultDTO.errorOf(2005, "问题不存在");
            }

            commentMapper.insert(comment);
            System.out.println(question);

            CommentExample example = new CommentExample();
            example.createCriteria().andParentIdEqualTo(comment.getParentId());
            long count = commentMapper.countByExample(example);
            System.out.println("parentId:"+comment.getParentId()+" count:" + count);
            question.setCommentCount(Math.toIntExact(count + 1));
            questionMapper.updateByPrimaryKey(question);

        }

        if(comment.getType() == 2){
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                return ResultDTO.errorOf(2004, "回复评论不存在");
            }


            commentMapper.insert(comment);

        }

        return ResultDTO.okOf();
    }

    public List<CommentDTO> listByQuestionId(Integer id,Integer type) {
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(Long.valueOf(id)).andTypeEqualTo(type);
        example.setOrderByClause("gmt_create desc");// 设置时间倒序
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0){
            return new ArrayList<>();
        }

        List<CommentDTO> list = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            CommentDTO commentDTO = new CommentDTO();
            Comment comment = comments.get(i);
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(String.valueOf(comment.getCommentator()));
            List<User> users = userMapper.selectByExample(userExample);
            commentDTO.setId(comment.getId());
            commentDTO.setUser(users.get(0));
            commentDTO.setLikeCount(comment.getLikeCount());
            commentDTO.setCommentator(Long.valueOf(comment.getCommentator()));
            commentDTO.setType(Long.valueOf(comment.getType()));
            commentDTO.setParentID(comment.getParentId());
            commentDTO.setGmtCreate(comment.getGmtCreate());
            commentDTO.setGmtModified(comment.getGmtModified());
            commentDTO.setContent(comment.getContent());
            list.add(commentDTO);
        }

        System.out.println(list);

        return list;
    }
}
