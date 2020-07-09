package top.youmunan.communty.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.youmunan.communty.model.User;
@Mapper
public interface UserMapper {
    @Insert("Insert into `user`(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("Select * from `user` where token=#{token}")
    User findByToken(String token);
}
