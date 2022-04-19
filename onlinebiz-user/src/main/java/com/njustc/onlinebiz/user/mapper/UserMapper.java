package com.njustc.onlinebiz.user.mapper;

import com.njustc.onlinebiz.user.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 通过用户名查询用户
     *
     * @param userName 待查询的用户名
     * @return 若查询成功，则返回user对象；若查询失败，则返回null
     */
    @Select("select * from user where user.userName=#{userName}")
    User queryUserByUserName(String userName);

    /**
     * 通过用户id查询用户
     *
     * @param userId 待查询的用户id
     * @return 若查询成功，则返回user对象；若查询失败，则返回null
     */
    @Select("select * from user where user.userId=#{userId}")
    User queryUserByUserId(int userId);

    /**
     * 保存用户信息至数据库
     *
     * @param userName 待保存用户的用户名
     * @param userPassword 待保存用户的用户密码
     */
    @Insert("insert into user(userName, userPassword) values(#{userName}, #{userPassword})")
    void saveUser(String userName, String userPassword);

    /**
     * 通过用户名与用户密码查询用户
     *
     * @param userName 待登录用户的用户名
     * @param userPassword 待登录用户的用户密码
     * @return 若查询成功，则返回User对象；若查询失败，则返回null
     */
    @Select("select * from user where user.userName=#{userName} and user.userPassword=#{userPassword}")
    User queryUserByUserNameAndUserPassword(String userName, String userPassword);
}
