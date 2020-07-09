package top.youmunan.communty.mapper;

import org.apache.ibatis.annotations.*;
import top.youmunan.communty.model.User;
@Mapper
public interface UserMapper {
    @Insert("Insert into `user`(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("Select * from `user` where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("Select * from `user` where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("Select * from `user` where account_id = #{id}")
    User findByAccountId(@Param("id") Integer id);

    @Update("Update `user` set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where account_id=#{accountId}")
    void updateUser(User user);
}
