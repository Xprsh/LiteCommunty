package top.youmunan.community.mapper;

import top.youmunan.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    List<Question> selectRelated(Question question);
}
