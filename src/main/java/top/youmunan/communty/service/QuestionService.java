package top.youmunan.communty.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.h2.mvstore.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.youmunan.communty.dto.PageDTO;
import top.youmunan.communty.dto.QuestionDTO;
import top.youmunan.communty.mapper.QuestionMapper;
import top.youmunan.communty.mapper.UserMapper;
import top.youmunan.communty.model.Question;
import top.youmunan.communty.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size){

        //size*(page-1)
        Integer offset = size * (page-1);
        List<Question> list = questionMapper.list(offset,size);

        List<QuestionDTO> dto = new ArrayList<>();
        for (Question question:list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            dto.add(questionDTO);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(dto);
        // 总页数
        if(questionMapper.getTotal() % size ==0){
            pageDTO.setTotalPage(questionMapper.getTotal() / size);
        }else {
            pageDTO.setTotalPage(questionMapper.getTotal() / size + 1);
        }
        // 当前页
        pageDTO.setCurrentPage(page);

        System.out.println(pageDTO);

        return pageDTO;
    }
}
