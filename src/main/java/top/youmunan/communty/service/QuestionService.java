package top.youmunan.communty.service;


import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.youmunan.communty.dto.PageDTO;
import top.youmunan.communty.dto.QuestionDTO;
import top.youmunan.communty.mapper.QuestionMapper;
import top.youmunan.communty.mapper.UserMapper;
import top.youmunan.communty.model.Question;
import top.youmunan.communty.model.QuestionExample;
import top.youmunan.communty.model.User;
import top.youmunan.communty.model.UserExample;

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

        List<Question> list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //List<Question> list = this.questionMapper.list(offset,size);

        List<QuestionDTO> dto = new ArrayList<>();
        for (Question question:list) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            dto.add(questionDTO);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(dto);
        // 总页数
        if(this.questionMapper.countByExample(new QuestionExample()) % size ==0){
            pageDTO.setTotalPage((int) (this.questionMapper.countByExample(new QuestionExample()) / size));
        }else {
            pageDTO.setTotalPage((int) (this.questionMapper.countByExample(new QuestionExample()) / size + 1));
        }
        // 当前页
        pageDTO.setCurrentPage(page);

        System.out.println(pageDTO);

        return pageDTO;
    }

    public PageDTO list(String id, Integer page, Integer size) {
        //size*(page-1)
        Integer offset = size * (page-1);
        List<Question> list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        List<QuestionDTO> dto = new ArrayList<>();
        for (Question question:list) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            dto.add(questionDTO);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(dto);
        // 总页数
        if((questionMapper.countByExample(new QuestionExample()) % size ==0)){
            pageDTO.setTotalPage((int) (questionMapper.countByExample(new QuestionExample()) / size));
        }else {
            pageDTO.setTotalPage((int) (questionMapper.countByExample(new QuestionExample()) / size + 1));
        }
        // 当前页
        pageDTO.setCurrentPage(page);

        System.out.println(pageDTO);
        System.out.println((int) (questionMapper.countByExample(new QuestionExample())));

        return pageDTO;
    }

    public QuestionDTO getTotalByQuestionId(Integer id) {
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(id);
        List<Question> questions = questionMapper.selectByExample(example);
        Question question = questions.get(0);
        System.out.println(question);
        if(question != null){
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(userExample);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            return questionDTO;
        }else {
            return null;
        }
    }



    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            questionMapper.insert(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.insert(question);
        }
    }
}
