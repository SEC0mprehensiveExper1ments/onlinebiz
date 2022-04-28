package com.njustc.onlinebiz.user;

import com.njustc.onlinebiz.user.mapper.UserMapper;
import com.njustc.onlinebiz.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.util.DigestUtils;

// IT 结尾表示一个集成测试。这里使用 @MyBatisTest 会自动 rollback，
// 并且不会启动 Spring 容器中的无关实例（最好是纯 MyBatis 测试，否则
// 可能会因为缺少依赖而失败？）
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperIT {

    @Autowired
    private UserMapper userMapper;

    private final User user = new User(1L, "Tom", DigestUtils.md5DigestAsHex("123".getBytes()), User.CUSTOMER_ROLE);

    @Test
    public void testInsertUserSuccess() {
        Assertions.assertEquals(1, userMapper.insertUser(user));
        Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
    }

    @Nested
    public class TestWithData {

        @BeforeEach
        public void prepare() {
            userMapper.insertUser(user);
        }

        @Test
        public void testSelectUserByIdFail() {
            Assertions.assertNull(userMapper.selectUserByUserId(10L));
        }

        @Test
        public void testSelectUserByUserNameFail() {
            Assertions.assertNull(userMapper.selectUserByUserName("not exists"));
        }

        @Test
        public void testUpdateUserNameFail() {
            Assertions.assertEquals(0, userMapper.updateUserNameById(10L, "Jack"));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testUpdateUserPasswordFail() {
            Assertions.assertEquals(0, userMapper.updateUserPasswordById(10L, "123"));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testUpdateUserRoleFail() {
            Assertions.assertEquals(0, userMapper.updateUserRoleById(10L, User.ADMIN_ROLE));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testDeleteUserByIdFail() {
            Assertions.assertEquals(0, userMapper.deleteUserById(10L));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testSelectUserByIdSuccess() {
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testSelectUserByUserNameSuccess() {
            Assertions.assertEquals(user, userMapper.selectUserByUserName(user.getUserName()));
        }

        @Test
        public void testUpdateUserNameSuccess() {
            String name = user.getUserName();
            Assertions.assertEquals(1, userMapper.updateUserNameById(user.getUserId(), "Jack"));
            Assertions.assertEquals("Jack", userMapper.selectUserByUserId(user.getUserId()).getUserName());
            Assertions.assertNull(userMapper.selectUserByUserName(name));
        }

        @Test
        public void testUpdateUserPasswordSuccess() {
            Assertions.assertEquals(1, userMapper.updateUserPasswordById(user.getUserId(), "abc"));
            Assertions.assertEquals("abc", userMapper.selectUserByUserId(user.getUserId()).getUserPassword());
        }

        @Test
        public void testUpdateUserRoleSuccess() {
            Assertions.assertEquals(1, userMapper.updateUserRoleById(user.getUserId(), User.ADMIN_ROLE));
            Assertions.assertEquals(User.ADMIN_ROLE, userMapper.selectUserByUserId(user.getUserId()).getUserRole());
        }

        @Test
        public void testDeleteUserByIdSuccess() {
            Assertions.assertEquals(1, userMapper.deleteUserById(user.getUserId()));
            Assertions.assertNull(userMapper.selectUserByUserId(user.getUserId()));
        }
    }
}