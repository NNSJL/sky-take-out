package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据OpenId查询用户
     * @param openId
     * @return
     */
    @Select("select * from user where id = #{openId}")
    User getByOpenId(String openId);

    /**
     * 插入数据，并告诉MyBatis使用JDBC中的getGeneratedKey来捕获自增长的主键值，并赋给user中的id属性
     * @param user
     */
    void insert(User user);
}
