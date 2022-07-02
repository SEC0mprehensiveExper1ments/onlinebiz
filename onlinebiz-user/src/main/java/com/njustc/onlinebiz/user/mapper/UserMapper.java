package com.njustc.onlinebiz.user.mapper;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户服务的数据访问层接口类
 */
@Mapper
public interface UserMapper {

    /**
     * 保存用户信息至数据库
     * @param user 要保存的 User 对象，除了 userId 以外的各个字段必须被正确设置。如果操作成功
     *             会把自增主键的值取回来，放在 user 对象的 userId 字段里。
     * @return 本次操作影响的行数，若成功应当返回 1；失败返回 0
     */
    int insertUser(User user);

    /**
     * 按用户ID查询用户信息
     * @param userId 用户ID
     * @return 成功返回该用户对象，失败返回 null
     */
    User selectUserByUserId(Long userId);

    /**
     * 按用户名查询用户信息
     * @param userName 用户名
     * @return 成功返回该用户对象，失败返回 null
     */
    User selectUserByUserName(String userName);

    /**
     * 根据用户ID更新用户名
     * @param userId 用户ID
     * @param userName 新的用户名
     * @return 本次操作影响的行数，若成功应当返回 1；失败返回 0
     */
    int updateUserNameById(Long userId, String userName);

    /**
     * 根据用户ID更新用户密码
     * @param userId 用户ID
     * @param userPassword 新的用户密码
     * @return 本次操作影响的行数，若成功应当返回 1；失败返回 0
     */
    int updateUserPasswordById(Long userId, String userPassword);

    /**
     * 根据用户名更新用户角色
     * @param userName 用户名
     * @param userRole 新的用户角色
     * @return 本次操作影响的行数，若成功应当返回 1；失败返回 0
     */
    int updateUserRoleByUserName(String userName, String userRole);

    /**
     * 根据用户ID删除用户
     * @param userId 要删除的用户ID
     * @return 本次操作影响的行数，若成功应当返回 1；失败返回 0
     */
    int deleteUserById(Long userId);

    /**
     * 根据模式字符串查找用户名与此相匹配的用户
     * @param pattern 模糊匹配的模式字符串
     * @return 匹配的用户列表
     */
    List<User> selectUsersWithUserNameLike(String pattern);

    /**
     * 根据用户角色查询用户
     * @param userRole 用户角色
     * @return 查询到的用户列表
     */
    List<User> selectUserByUserRole(Role userRole);

}
