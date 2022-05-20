package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.user.mapper.UserMapper;
import com.njustc.onlinebiz.common.model.User;
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
    public boolean createUser(String userName, String userPassword) {
        if (userPassword == null || userName == null || !userName.matches(USERNAME_PATTERN)) {
            return false;
        } else if (userMapper.selectUserByUserName(userName) != null) {
            // 用户名已存在
            return false;
        }
        // 强哈希加密
        String passwordEncoded = passwordEncoder.encode(userPassword);
        User user = new User(userName, passwordEncoded, Role.CUSTOMER);
        try {
            return userMapper.insertUser(user) == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateCurrentUserName(String userName, HttpServletRequest request) {
        // 检查新用户名的合法性
        if (userName == null || !userName.matches(USERNAME_PATTERN) ||
                userMapper.selectUserByUserName(userName) != null) {
            return false;
        }
        // 检查会话数据
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId());
        if (user == null) {
            // 不在登录状态
            return false;
        }
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
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean updateCurrentUserPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        // 检查数据合法性
        if (oldPassword == null || newPassword == null) {
            return false;
        }
        // 检查会话数据
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId());
        if (user == null) {
            return false;
        }
        // 检查旧密码是否一致
        if (!passwordEncoder.matches(oldPassword, user.getUserPassword())) {
            return false;
        }
        // 更新数据库
        String newPasswordEncoded = passwordEncoder.encode(newPassword);
        if (userMapper.updateUserPasswordById(user.getUserId(), newPasswordEncoded) == 1) {
            // 更新会话数据
            user.setUserPassword(newPasswordEncoded);
            redisTemplate.opsForHash().put(SESSION_ID_TO_USER, session.getId(), user);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId());
        if (user == null) {
            return false;
        }
        if (userMapper.deleteUserById(user.getUserId()) == 1) {
            // 删除会话数据和其它映射关系
            redisTemplate.opsForHash().delete(SESSION_ID_TO_USER, session.getId());
            redisTemplate.opsForHash().delete(USERNAME_TO_SESSION_ID, user.getUserName());
            return true;
        }
        return false;
    }

    @Override
    public boolean handleLogIn(String userName, String userPassword, HttpServletRequest request) {
        // 判断是否已经登录
        HttpSession session = request.getSession(false);
        if (session == null) {
            // 创建 session
            session = request.getSession(true);
        } else if (redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId()) != null) {
            // 已经登录，清除当前登录信息
            redisTemplate.opsForHash().delete(SESSION_ID_TO_USER, session.getId());
        }
        User user = userMapper.selectUserByUserName(userName);
        if (user == null || !passwordEncoder.matches(userPassword, user.getUserPassword())) {
            // 用户名或密码错误
            return false;
        }
        // 将用户的基本信息（包括身份）保存在 Redis 中
        redisTemplate.opsForHash().put(SESSION_ID_TO_USER, session.getId(), user);
        // 保存用户名到会话ID的映射关系
        redisTemplate.opsForHash().put(USERNAME_TO_SESSION_ID, user.getUserName(), session.getId());
        return true;
    }

    @Override
    public boolean handleLogOut(HttpServletRequest request) {
        // 判断是否已经登录
        HttpSession session = request.getSession(false);
        if (session == null) {
            // 不在登录状态
            return false;
        }
        User user = (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId());
        if (user == null) {
            return false;
        }
        // 删除会话数据
        redisTemplate.opsForHash().delete(SESSION_ID_TO_USER, session.getId());
        // 删除用户名到会话ID的映射
        redisTemplate.opsForHash().delete(USERNAME_TO_SESSION_ID, user.getUserName());
        return true;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        // 从 Redis 获取用户信息
        return (User) redisTemplate.opsForHash().get(SESSION_ID_TO_USER, session.getId());
    }

    // 暂时不分页，看后续需求
    @Override
    public List<User> searchUserByUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            // 不合法的搜索条件
            return null;
        }
        // 子串匹配
        String pattern = "%" + userName + "%";
        return userMapper.selectUsersWithUserNameLike(pattern);
    }

    @Override
    public boolean updateUserRole(String userName, String newValue, Role userRole) {
        // 检查参数和权限
        if (userName == null || !userName.matches(USERNAME_PATTERN) || userRole != Role.ADMIN) {
            return false;
        }
        // 检查新的用户角色是否合法
        Role newRole;
        try {
            newRole = Role.valueOf(newValue);
        } catch (Exception e) {
            return false;
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
            return true;
        }
        return false;
    }

    @Override
    public User getUserByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return userMapper.selectUserByUserId(userId);
    }

}
