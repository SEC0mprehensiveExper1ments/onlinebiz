package com.njustc.onlinebiz.user.service;

import com.njustc.onlinebiz.user.mapper.UserMapper;
import com.njustc.onlinebiz.user.model.User;
import org.apache.el.stream.StreamELResolverImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// 这里对数据记录的存在性做了额外校验，而实际上根据 MyBatis 的返回值
// 我们就可以知道操作是否成功。考虑到查询数据库可以用 Redis 缓存优化，
// 因此在实际操作之前先 find 一下可以在一定程度上减少非法请求访问数据
// 库的次数。目前的实现在缓存清除上还比较粗糙，看后续优化需求吧...
@Service
public class UserServiceImpl implements UserService {

    // 用户名只能由大小写字母、数字或下划线组成
    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{1,32}$";

    // 保存在 redis 中的用户信息的名字（用这个名字可以从 redis 取到用户信息）
    private static final String USER_SESSION_FIELD = "user-basic";

    UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Cacheable("user-by-id")
    public User findUserByUserId(Long userId) {
        return userId == null ? null : userMapper.selectUserByUserId(userId);
    }

    @Override
    @Cacheable("user-by-username")
    public User findUserByUserName(String userName) {
        if (userName == null || !userName.matches(USERNAME_PATTERN)) {
            // 用户名为空或者不符合要求
            return null;
        }
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user-by-id", allEntries = true, condition = "#result == true"),
            @CacheEvict(value = "user-by-username", key = "#root.args[0]", condition = "#result == true")
    })
    public boolean createUser(String userName, String userPassword) {
        if (userPassword == null || userName == null || !userName.matches(USERNAME_PATTERN)) {
            return false;
        } else if (findUserByUserName(userName) != null) {
            // 用户名已存在
            return false;
        }
        // 用 md5 加密明文密码，得到 32 字节的结果
        String passwordEncoded = DigestUtils.md5DigestAsHex(userPassword.getBytes());
        User user = new User(userName, passwordEncoded, User.GUEST_ROLE);
        return userMapper.insertUser(user) == 1;
    }

    @Override
    @Caching (evict = {
        @CacheEvict(value = "user-by-id", allEntries = true, condition = "#result == true"),
        @CacheEvict(value = "user-by-username", key = "#root.args[0]", condition = "#result == true")
    })
    public boolean updateUserName(String userName, HttpServletRequest request) {
        // 检查新用户名的合法性
        if (userName == null || !userName.matches(USERNAME_PATTERN) ||
                findUserByUserName(userName) != null) {
            return false;
        }
        // 检查会话数据
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute(USER_SESSION_FIELD);
        if (user == null) {
            // 不在登录状态
            return false;
        }
        // 更新数据库
        if (userMapper.updateUserNameById(user.getUserId(), userName) == 1) {
            // 更新会话
            user.setUserName(userName);
            session.setAttribute(USER_SESSION_FIELD, user);
            return true;
        }
        // 写入数据库失败
        return false;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user-by-id", allEntries = true, condition = "#result == true"),
            @CacheEvict(value = "user-by-username", allEntries = true, condition = "#result == true")
    })
    public boolean updateUserPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        // 检查数据合法性
        if (oldPassword == null || newPassword == null) {
            return false;
        }
        // 检查会话数据
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute(USER_SESSION_FIELD);
        if (user == null) {
            return false;
        }
        // 检查旧密码是否一致
        String oldPasswordEncoded = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldPasswordEncoded.equals(user.getUserPassword())) {
            return false;
        }
        // 更新数据库
        String newPasswordEncoded = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        if (userMapper.updateUserPasswordById(user.getUserId(), newPasswordEncoded) == 1) {
            // 更新会话
            user.setUserPassword(newPasswordEncoded);
            session.setAttribute(USER_SESSION_FIELD, user);
            return true;
        }
        return false;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user-by-id", allEntries = true, condition = "#result == true"),
            @CacheEvict(value = "user-by-username", allEntries = true, condition = "#result == true")
    })
    public boolean removeUser(HttpServletRequest request) {
        HttpSession session = null;
        User user = null;
        if ((session = request.getSession(false)) == null ||
                (user = (User) session.getAttribute(USER_SESSION_FIELD)) == null) {
            return false;
        }
        if (userMapper.deleteUserById(user.getUserId()) == 1) {
            session.removeAttribute(USER_SESSION_FIELD);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleLogIn(String username, String password, HttpServletRequest request) {
        // 判断是否已经登录
        HttpSession session = request.getSession(false);
        if (session == null) {
            // 创建 session
            session = request.getSession(true);
        } else if (session.getAttribute(USER_SESSION_FIELD) != null) {
            // 已经登录，清除当前登录信息
            session.removeAttribute(USER_SESSION_FIELD);
        }

        User user = findUserByUserName(username);
        String passwordEncoded = DigestUtils.md5DigestAsHex(password.getBytes());
        if (user == null || !passwordEncoded.equals(user.getUserPassword())) {
            // 用户名或密码错误
            return false;
        }

        // 将用户的基本信息（包括身份）保存在 redis 中
        session.setAttribute(USER_SESSION_FIELD, user);
        return true;
    }

    @Override
    public boolean handleLogOut(HttpServletRequest request) {
        // 判断是否已经登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_SESSION_FIELD) == null) {
            // 不在登录状态
            return false;
        }
        // 清除登录信息
        session.removeAttribute(USER_SESSION_FIELD);
        return true;
    }

    @Override
    public boolean checkLogIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_SESSION_FIELD) != null;
    }

    @Override
    public User getUserIdentity(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        // 从 redis 获取用户信息
        return (User) session.getAttribute(USER_SESSION_FIELD);
    }
}
