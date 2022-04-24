package com.njustc.onlinebiz.user;

import com.njustc.onlinebiz.user.mapper.UserMapper;
import com.njustc.onlinebiz.user.model.User;
import com.njustc.onlinebiz.user.service.UserService;
import com.njustc.onlinebiz.user.service.UserServiceImpl;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;
import org.springframework.util.DigestUtils;

// 这里使用 Mock 对业务层进行单元测试；单元测试原则上应当遵守
// 自动、独立、可重复，因此我们使用 Mock 来模拟 DAO 层。
public class UserServiceTest {

    private final UserMapper userMapper = mock(UserMapper.class);

    private final UserService userService = new UserServiceImpl(userMapper);

    // 准备两条数据记录
    private final User userTom = new User(1L, "Tom", DigestUtils.md5DigestAsHex("123".getBytes()), User.GUEST_ROLE);

    // 下面每个测试之前都要执行这个方法，来准备一个模拟的 UserMapper 对象
    // 并注入到 UserService 实例中
    @BeforeEach
    public void prepareMockedUserMapper() {
        when(userMapper.selectUserByUserId(userTom.getUserId())).thenReturn(userTom);
        when(userMapper.selectUserByUserName(userTom.getUserName())).thenReturn(userTom);
    }

    @Test
    public void testFindExistingUserById() {
        Assertions.assertSame(userTom, userService.findUserByUserId(userTom.getUserId()));
    }

    @Test
    public void testFindNonExistingUserById() {
        when(userMapper.selectUserByUserId(2L)).thenReturn(null);
        Assertions.assertSame(null, userService.findUserByUserId(2L));
    }

    @Test
    public void testFindExistingUserByUserName() {
        Assertions.assertSame(userTom, userService.findUserByUserName(userTom.getUserName()));
    }

    @Test
    public void testFindNonExistingUserByUserName() {
        when(userMapper.selectUserByUserName("jack")).thenReturn(null);
        Assertions.assertSame(null, userService.findUserByUserName("jack"));
    }

    // 嵌套测试，用于测试创建用户的一组情况
    // 未必是个好的实践
    @Nested
    public class TestCreateUser {

        @BeforeEach
        public void prepare() {
            when(userMapper.insertUser(any())).thenReturn(1);
        }

        @Test
        public void testCreateExistingUser() {
            Assertions.assertFalse(userService.createUser(userTom.getUserName(), "123"));
        }

        @Test
        public void testCreateUserWithInvalidUsername() {
            Assertions.assertFalse(userService.createUser("@#$&*", "123"));
        }

        @Test
        public void testCreateValidUser() {
            Assertions.assertTrue(userService.createUser("anotherUser", "321"));
        }

    }

    @Nested
    public class TestUpdateUser {

        User userJack = new User(2L, "Jack", DigestUtils.md5DigestAsHex("abc".getBytes()), User.CUSTOMER_ROLE);

        @BeforeEach
        public void prepare() {
            when(userMapper.selectUserByUserId(userJack.getUserId())).thenReturn(userJack);
            when(userMapper.selectUserByUserName(userJack.getUserName())).thenReturn(userJack);
        }

        @Test
        public void testUpdateUsernameWhenNotExists() {
            when(userMapper.selectUserByUserId(2L)).thenReturn(null);
            Assertions.assertFalse(userService.updateUserName(2L, "abc"));
        }

        @Test
        public void testUpdateUsernameWithInvalid() {
            Assertions.assertFalse(userService.updateUserName(userTom.getUserId(), "!#%^&$"));
        }

        @Test
        public void testUpdateUsernameWithExisting() {
            Assertions.assertFalse(userService.updateUserName(userTom.getUserId(), userJack.getUserName()));
        }

        @Test
        public void testUpdateUsernameWithOriginal() {
            Assertions.assertTrue(userService.updateUserName(userTom.getUserId(), userTom.getUserName()));
        }

        @Test
        public void testUpdateUsernameWithNew() {
            when(userMapper.updateUserNameById(userTom.getUserId(), "JackSwagger")).thenReturn(1);
            Assertions.assertTrue(userService.updateUserName(userTom.getUserId(), "JackSwagger"));
        }

        @Test
        public void testUpdatePasswordWhenNotExists() {
            when(userMapper.selectUserByUserId(2L)).thenReturn(null);
            Assertions.assertFalse(userService.updateUserPassword(2L, "123", "newPass"));
        }

        @Test
        public void testUpdatePasswordWithWrongOld() {
            Assertions.assertFalse(userService.updateUserPassword(userTom.getUserId(), "321", "newPass"));
        }

        @Test
        public void testUpdatePasswordSuccessfully() {
            when(userMapper.updateUserPasswordById(userTom.getUserId(), DigestUtils.md5DigestAsHex("newPass".getBytes()))).thenReturn(1);
            Assertions.assertTrue(userService.updateUserPassword(userTom.getUserId(), "123", "newPass"));
        }

    }

    @Test
    public void testRemoveExistingUser() {
        when(userMapper.deleteUserById(userTom.getUserId())).thenReturn(1);
        Assertions.assertTrue(userService.removeUser(userTom.getUserId()));
    }

    @Test
    public void testRemoveNonExistingUser() {
        when(userMapper.selectUserByUserId(2L)).thenReturn(null);
        when(userMapper.deleteUserById(2L)).thenReturn(0);
        Assertions.assertFalse(userService.removeUser(2L));
    }
}
