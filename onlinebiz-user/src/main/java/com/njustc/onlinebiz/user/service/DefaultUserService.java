package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.exception.UserDBFailureException;
import com.njustc.onlinebiz.user.exception.UserInvalidArgumentException;
import com.njustc.onlinebiz.user.exception.UserPermissonDeniedException;
import com.njustc.onlinebiz.user.mapper.UserMapper;
import com.njustc.onlinebiz.user.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

// 用户管理的并发访问量应该不是很大，这里先不用Redis缓存数据库查询结果，
// 降低整体复杂度
@Service
public class DefaultUserService implements UserService {

    // 用户名只能由大小写字母、数字或下划线组成
    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{1,32}$";

    // 保存在会话中的用户数据名称
    private static final String SESSION_ID_TO_USER = "sessionId2user";

    // 用户名到用户会话ID的映射名称
    private final String USERNAME_TO_SESSION_ID = "userName2sessionId";

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<Object, Object> redisTemplate;

    public DefaultUserService(UserMapper userMapper, PasswordEncoder passwordEncoder, RedisTemplate<Object, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public void createUser(String userName, String userPassword) {
        if (userPassword == null || userName == null || !userName.matches(USERNAME_PATTERN)) {
            throw new UserInvalidArgumentException("不合法的用户名或密码参数");
        } else if (userMapper.selectUserByUserName(userName) != null) {
            // 用户名已存在
            throw new UserInvalidArgumentException("用户名重复");
        }
        // 强哈希加密
        String passwordEncoded = passwordEncoder.encode(userPassword);
        User user = new User(userName, passwordEncoded, Role.CUSTOMER);
        try {
            if (userMapper.insertUser(user) != 1) {
                throw new UserDBFailureException("插入用户失败");
            }
        } catch (Exception e) {
            throw new UserDBFailureException("用户名重复");
        }
    }

    @Override
    @Transactional
    public void updateCurrentUserName(String userName, HttpServletRequest request) {
        // 检查新用户名的合法性
        if (userName == null || !userName.matches(USERNAME_PATTERN)) {
            throw new UserInvalidArgumentException("用户名参数不合法");
        } else if (userMapper.selectUserByUserName(userName) != null) {
            throw new UserInvalidArgumentException("用户名重复");
        }
        // 获取当前用户信息
        User user = getCurrentUser(request);
        HttpSession session = request.getSession(false);
        // 更新数据库
        try {
            if (userMapper.updateUserNameById(user.getUserId(), userName) == 1) {
                // 删除原来用户名到会话ID的映射
                redisTemplate.opsForHash().delete(USERNAME_TO_SESSION_ID, user.getUserName());
                // 更新会话数据
                user.setUserName(userName);
                redisTemplate.opsForHash().put(SESSION_ID_TO_USER, session.getId(), user);
                // 添加新的用户名到会话ID的映射
                redisTemplate.opsForHash().put(USERNAME_TO_SESSION_ID, user.getUserName(), session.getId());
            } else {
                throw new UserDBFailureException("修改用户名失败");
            }
        } catch (Exception e) {
            throw new UserInvalidArgumentException("用户名重复");
        }
    }

    @Override
    public void updateCurrentUserPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        // 检查数据合法性
        if (oldPassword == null || newPassword == null) {
            throw new UserInvalidArgumentException("新、旧密码均不能为空");
        }
        // 获取当前用户信息
        User user = getCurrentUser(request);
        HttpSession session = request.getSession(false);
        // 检查旧密码是否一致
        if (!passwordEncoder.matches(oldPassword, user.getUserPassword())) {
            throw new UserPermissonDeniedException("旧密码不正确");
        }
        // 更新数据库
        String newPasswordEncoded = passwordEncoder.encode(newPassword);
        if (userMapper.updateUserPasswordById(user.getUserId(), newPasswordEncoded) == 1) {
            // 更新会话数据
            user.setUserPassword(newPasswordEncoded);
            redisTemplate.opsForHash().put(SESSION_ID_TO_USER, session.getId(), user);
        } else {
            throw new UserDBFailureException("修改用户密码失败");
        }
    }

    @Override
    public void removeCurrentUser(HttpServletRequest request) {
        User user = getCurrentUser(request);
        HttpSession session = request.getSession(false);
        if (userMapper.deleteUserById(user.getUserId()) == 1) {
            // 删除会话数据和其它映射关系
            redisTemplate.opsForHash().delete(SESSION_ID_TO_USER, session.getId());
            redisTemplate.opsForHash().delete(USERNAME_TO_SESSION_ID, user.getUserName());
        } else {
            throw new UserDBFailureException("注销当前用户失败");
        }
    }

    @Override
    public void handleLogIn(String userName, String userPassword, HttpServletRequest request) {
        // 判断是否已经登录
        HttpSession session = request.getSession(false);
        if (session == null) {
            // 创建 session
            session = request.getSession(true);
        } else if (redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId()) != null) {
            // 已经登录的情况下禁止登录另一个账号
            throw new UserPermissonDeniedException("请先退出当前账号再登录");
        }
        User user = userMapper.selectUserByUserName(userName);
        if (user == null || !passwordEncoder.matches(userPassword, user.getUserPassword())) {
            // 用户名或密码错误
            throw new UserPermissonDeniedException("用户名或密码错误");
        }
        // 将用户的基本信息（包括身份）保存在 Redis 中
        redisTemplate.opsForHash().put(SESSION_ID_TO_USER, session.getId(), user);
        // 保存用户名到会话ID的映射关系
        redisTemplate.opsForHash().put(USERNAME_TO_SESSION_ID, user.getUserName(), session.getId());
    }

    @Override
    public void handleLogOut(HttpServletRequest request) {
        // 判断是否已经登录
        User user = getCurrentUser(request);
        HttpSession session = request.getSession(false);
        // 删除会话数据
        redisTemplate.opsForHash().delete(SESSION_ID_TO_USER, session.getId());
        // 删除用户名到会话ID的映射
        redisTemplate.opsForHash().delete(USERNAME_TO_SESSION_ID, user.getUserName());
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UserPermissonDeniedException("尚未登录");
        }
        // 从 Redis 获取用户信息
        User user = (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId());
        if (user == null) {
            throw new UserPermissonDeniedException("尚未登录");
        }
        return user;
    }

    // 暂时不分页，看后续需求
    @Override
    public List<User> searchUserByUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            // 不合法的搜索条件
            throw new UserInvalidArgumentException("用户名参数不合法");
        }
        // 子串匹配
        String pattern = "%" + userName + "%";
        return userMapper.selectUsersWithUserNameLike(pattern);
    }

    @Override
    public List<User> searchUserByUserRole(Role userRole) {
        if (userRole == null) {
            throw new UserInvalidArgumentException("用户角色缺失");
        }
        return userMapper.selectUserByUserRole(userRole);
    }

    @Override
    public void updateUserRole(String userName, String newValue, Role userRole) {
        // 检查参数和权限
        if (userName == null || !userName.matches(USERNAME_PATTERN)) {
            throw new UserInvalidArgumentException("用户名参数不合法");
        } else if (userRole != Role.ADMIN) {
            throw new UserPermissonDeniedException("无权修改用户角色");
        }
        // 检查新的用户角色是否合法
        Role newRole;
        try {
            newRole = Role.valueOf(newValue);
        } catch (Exception e) {
            throw new UserInvalidArgumentException("新角色值不合法");
        }
        // 写入数据库
        if (userMapper.updateUserRoleByUserName(userName, newValue) == 1) {
            // 如果该用户处于登录状态，更新会话数据
            String sessionId = (String) redisTemplate.opsForHash().get(USERNAME_TO_SESSION_ID, userName);
            if (sessionId != null) {
                User user = (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, sessionId);
                if (user != null) {
                    user.setUserRole(newRole);
                    redisTemplate.opsForHash().put(SESSION_ID_TO_USER, sessionId, user);
                }
            }
        } else {
            throw new UserDBFailureException("更新用户角色失败");
        }
    }

    @Override
    public User getUserByUserId(Long userId) {
        if (userId == null) {
            throw new UserInvalidArgumentException("用户ID不能为空");
        }
        User user = userMapper.selectUserByUserId(userId);
        if (user == null) {
            throw new UserInvalidArgumentException("不存在的用户ID");
        }
        return user;
    }

}
