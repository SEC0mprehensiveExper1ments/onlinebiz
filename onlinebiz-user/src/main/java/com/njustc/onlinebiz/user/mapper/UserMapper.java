package com.njustc.onlinebiz.user.mapper;

import com.njustc.onlinebiz.user.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectUserByUserId(Long userId);

    User selectUserByUserName(String userName);

    /**
     * 保存用户信息至数据库
     *
     * @param user 要保存的 User 对象，除了 userId 以外的各个字段必须被正确设置。如果操作成功
     *             会把自增主键的值取回来，放在 user 对象的 userId 字段里。
     * @return 本次操作影响的行数，若成功应当返回 1；失败返回 0
     */
    int insertUser(User user);

    int updateUserNameById(Long userId, String userName);

    int updateUserPasswordById(Long userId, String userPassword);

    int updateUserRoleById(Long userId, String userRole);

    int deleteUserById(Long userId);

}
