package top.youmunan.community.service;


import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.youmunan.community.dto.HotQuestionDTO;
import top.youmunan.community.dto.PageDTO;
import top.youmunan.community.dto.QuestionDTO;
import top.youmunan.community.dto.RelatedQuestionDTO;
import top.youmunan.community.exception.CustomizeException;
import top.youmunan.community.mapper.QuestionExtMapper;
import top.youmunan.community.mapper.QuestionMapper;
import top.youmunan.community.mapper.UserMapper;
import top.youmunan.community.model.Question;
import top.youmunan.community.model.QuestionExample;
import top.youmunan.community.model.User;
import top.youmunan.community.model.UserExample;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PageDTO list(Integer page, Integer size) {

        //size*(page-1)
        Integer offset = size * (page - 1);

        QuestionExample example1 = new QuestionExample();
        example1.setOrderByClause("gmt_modified DESC");
        List<Question> list = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        //List<Question> list = this.questionMapper.list(offset,size);

        List<QuestionDTO> dto = new ArrayList<>();
        for (Question question : list) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setCommentCount(question.getCommentCount());
            questionDTO.setLikeCount(question.getLikeCount());
            questionDTO.setViewCount(question.getViewCount());
            questionDTO.setUser(user);
            dto.add(questionDTO);
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(dto);
        // 总页数
        if (this.questionMapper.countByExample(new QuestionExample()) % size == 0) {
            pageDTO.setTotalPage((int) (this.questionMapper.countByExample(new QuestionExample()) / size));
        } else {
            pageDTO.setTotalPage((int) (this.questionMapper.countByExample(new QuestionExample()) / size + 1));
        }
        // 当前页
        pageDTO.setCurrentPage(page);

        return pageDTO;
    }

    public PageDTO list(String id, Integer page, Integer size) {
        //size*(page-1)
        Integer offset = size * (page - 1);
        QuestionExample example1 = new QuestionExample();
        example1.setOrderByClause("gmt_modified DESC");
        List<Question> list = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));

        List<QuestionDTO> dto = new ArrayList<>();
        for (Question question : list) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setCommentCount(question.getCommentCount());
            questionDTO.setLikeCount(question.getLikeCount());
            questionDTO.setViewCount(question.getViewCount());
            questionDTO.setUser(user);
            dto.add(questionDTO);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(dto);
        // 总页数
        if ((questionMapper.countByExample(new QuestionExample()) % size == 0)) {
            pageDTO.setTotalPage((int) (questionMapper.countByExample(new QuestionExample()) / size));
        } else {
            pageDTO.setTotalPage((int) (questionMapper.countByExample(new QuestionExample()) / size + 1));
        }
        // 当前页
        pageDTO.setCurrentPage(page);

        return pageDTO;
    }

    public QuestionDTO getTotalByQuestionId(Integer id) {
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(id);
        List<Question> questions = questionMapper.selectByExample(example);
        Question question = questions.get(0);
        System.out.println(question);
        if (question != null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(userExample);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setCommentCount(question.getCommentCount());
            questionDTO.setLikeCount(question.getLikeCount());
            questionDTO.setViewCount(question.getViewCount());
            questionDTO.setUser(user);
            System.out.println(questionDTO);
            return questionDTO;
        } else {
            throw new CustomizeException("问题不存在");
        }
    }


    public void createOrUpdate(Question question) {
        System.out.println(question);
        if (question.getId() == null) {
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setCommentCount(0);
            questionMapper.insertSelective(question);
        } else {
            Question oldQuestion = questionMapper.selectByPrimaryKey(question.getId());
            oldQuestion.setGmtModified(System.currentTimeMillis());
            oldQuestion.setTitle(question.getTitle());
            oldQuestion.setTags(question.getTags());
            oldQuestion.setDetail(question.getDetail());

            int i = questionMapper.updateByPrimaryKey(oldQuestion);
            if (i == 0) {
                throw new CustomizeException("问题不存在，可能已被删除");
            }
        }
    }

    public synchronized void addViewCount(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setViewCount(question.getViewCount() + 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(id);
        questionMapper.updateByExampleSelective(question, example);
    }

    public List<RelatedQuestionDTO> relatedQuestion(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        List<Question> questions = questionExtMapper.selectRelated(question);
        if(questions.size() == 0){
            return null;
        }
        List<RelatedQuestionDTO> list = new ArrayList<>();
        for (Question q: questions) {
            RelatedQuestionDTO dto = new RelatedQuestionDTO();
            dto.setId(q.getId());
            dto.setTitle(q.getTitle());
            list.add(dto);
        }
        return list;
    }

    public List<HotQuestionDTO> hotQuestion(){
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("comment_count desc");
        RowBounds rowBounds = new RowBounds(0,20);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
        if(questions.size() == 0){
            return null;
        }
        List<HotQuestionDTO> list = new ArrayList<>();
        for (Question q: questions) {
            HotQuestionDTO dto = new HotQuestionDTO();
            dto.setId(q.getId());
            dto.setTitle(q.getTitle());
            list.add(dto);
        }
        return list;
    }

    /**
     *
     * @param key
     * @param type 1为标题关键字搜索，2为标签关键字搜索
     * @param page
     * @param size
     * @return
     */
    public PageDTO searchList(String key, Integer type, Integer page, Integer size) {

        //size*(page-1)
        Integer offset = size * (page - 1);

        QuestionExample example1 = new QuestionExample();
       if(type == 1){
           example1.createCriteria().andTitleLike("%"+ key +"%");
       }
       if (type == 2){
           example1.createCriteria().andTagsLike("%"+ key +"%");
       }
        example1.setOrderByClause("gmt_modified DESC");
        List<Question> list = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        //List<Question> list = this.questionMapper.list(offset,size);

        List<QuestionDTO> dto = new ArrayList<>();
        for (Question question : list) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(question.getCreator()));
            List<User> users = userMapper.selectByExample(example);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setCommentCount(question.getCommentCount());
            questionDTO.setLikeCount(question.getLikeCount());
            questionDTO.setViewCount(question.getViewCount());
            questionDTO.setUser(user);
            dto.add(questionDTO);
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(dto);
        // 总页数
        if (this.questionMapper.countByExample(example1) % size == 0) {
            pageDTO.setTotalPage((int) (this.questionMapper.countByExample(example1) / size));
        } else {
            pageDTO.setTotalPage((int) (this.questionMapper.countByExample(example1)) / size + 1);
        }


        // 当前页
        pageDTO.setCurrentPage(page);

        return pageDTO;
    }
}
