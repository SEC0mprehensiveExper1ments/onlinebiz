package com.njustc.onlinebiz.user;

import com.njustc.onlinebiz.common.model.Role;
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

import java.util.List;

// IT 结尾表示一个集成测试。这里使用 @MyBatisTest 会自动 rollback，
// 并且不会启动 Spring 容器中的无关实例（最好是纯 MyBatis 测试，否则
// 可能会因为缺少依赖而失败？）
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperIT {

    @Autowired
    private UserMapper userMapper;

    private final User user = new User(1L, "Tom", DigestUtils.md5DigestAsHex("123".getBytes()), Role.CUSTOMER);

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
            Assertions.assertNull(userMapper.selectUserByUserId(-1L));
        }

        @Test
        public void testSelectUserByUserNameFail() {
            Assertions.assertNull(userMapper.selectUserByUserName("not exists"));
        }

        @Test
        public void testUpdateUserNameFail() {
            Assertions.assertEquals(0, userMapper.updateUserNameById(-1L, "Jack"));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testUpdateUserPasswordFail() {
            Assertions.assertEquals(0, userMapper.updateUserPasswordById(-1L, "123"));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testUpdateUserRoleFail() {
            Assertions.assertEquals(0, userMapper.updateUserRoleByUserName("non-existing-userName", Role.CUSTOMER.toString()));
            Assertions.assertEquals(user, userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testDeleteUserByIdFail() {
            Assertions.assertEquals(0, userMapper.deleteUserById(-1L));
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
            Assertions.assertEquals(1, userMapper.updateUserRoleByUserName(user.getUserName(), Role.MARKETER.toString()));
            Assertions.assertEquals(Role.MARKETER, userMapper.selectUserByUserId(user.getUserId()).getUserRole());
        }

        @Test
        public void testSearchByUserNameShouldBeEmpty() {
            Assertions.assertEquals(0, userMapper.selectUsersWithUserNameLike("%non-exists%").size());
        }

        @Test
        public void testSearchByUserNameShouldNotBeEmpty() {
            Assertions.assertNotEquals(0, userMapper.selectUsersWithUserNameLike("%o%").size());
        }

        @Test
        public void testDeleteUserByIdSuccess() {
            Assertions.assertEquals(1, userMapper.deleteUserById(user.getUserId()));
            Assertions.assertNull(userMapper.selectUserByUserId(user.getUserId()));
        }

        @Test
        public void testSearchByUserRoleShouldBeEmpty() {
            Assertions.assertEquals(0, userMapper.selectUserByUserRole(Role.QA_SUPERVISOR).size());
        }

        @Test
        public void testSearchByUserRoleShouldNotBeEmpty() {
            List<User> userList = userMapper.selectUserByUserRole(Role.CUSTOMER);
            Assertions.assertEquals(1, userList.size());
            Assertions.assertEquals(user, userList.get(0));
        }
    }
}
