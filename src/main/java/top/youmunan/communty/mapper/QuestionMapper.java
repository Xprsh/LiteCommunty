package top.youmunan.communty.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;
import top.youmunan.communty.dto.QuestionDTO;
import top.youmunan.communty.model.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("Insert into `question`(title,detail,creator,gmt_create,gmt_modified,tags) values(#{title},#{detail},#{creator},#{gmtCreate},#{gmtModified},#{tags})")
    public void createQuestion(Question question);

    @Select("Select * from `question` limit #{offset},#{size}")
    public List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from `QUESTION`")
    public Integer getTotal();

    @Select("select count(1) from `QUESTION` where creator=#{id}")
    public Integer getTotalById(@Param("id") String id);

    @Select("select count(1) from `QUESTION` where creator=#{id}")
    public Integer getTotalByAccountId(@Param("id") String id);

    @Select("Select * from `question` where creator=#{id} limit #{offset},#{size}")
    List<Question> listById(@Param("id") String id, @Param("offset") Integer offset, @Param("size") Integer size);
}
