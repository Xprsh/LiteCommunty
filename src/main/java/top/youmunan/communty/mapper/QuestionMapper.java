package top.youmunan.communty.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.youmunan.communty.dto.QuestionDTO;
import top.youmunan.communty.model.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("Insert into `question`(title,detail,creator,gmt_create,gmt_modified,tags) values(#{title},#{detail},#{creator},#{gmtCreate},#{gmtModified},#{tags})")
    public void createQuestion(Question question);

    @Select("Select * from `question` limit #{offset},#{size}")
    public List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(*) from `QUESTION`")
    public Integer getTotal();
}
