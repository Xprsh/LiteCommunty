package top.youmunan.communty.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import top.youmunan.communty.model.Question;

@Mapper
public interface QuestionMapper {
    @Insert("Insert into `question`(title,detail,creator,gmt_create,gmt_modified,tags) values(#{title},#{detail},#{creator},#{gmtCreate},#{gmtModified},#{tags})")
    public void createQuestion(Question question);
}
