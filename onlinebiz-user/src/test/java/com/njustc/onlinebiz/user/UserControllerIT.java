package com.njustc.onlinebiz.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

// 在这里我们要启动整个应用，然后通过 WebTestClient 的实例向 Controller
// 发送请求并检验响应状态。由于我们不是在浏览器环境下测试，所以我们需要手动
// 为后续每个请求加上 cookie，并且由于每个测试用例使用一个不同的测试类实例，
// 我们需要把测试间共享的数据放在静态域中。
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIT {

    // 用来发送请求的对象
    private static final WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8001/api").responseTimeout(Duration.ofDays(1)).build();

    // 会话ID的cookie名称
    private static final String sessionCookieName = "NJUSTC_ONLINEBIZ_SESSION";

    // 保存会话 id 用于后面的测试
    private static String sessionId;

    // 保存一位用户信息用于测试
    private static UserDto userDto;

    @Test
    @Order(1)
    public void testLogInWithFakeAccountInURL() {
        // 参数放在 url 上
        client.post()
                .uri("/login?userName=tester&userPassword=tester")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(2)
    public void testLogInWithFakeAccountInBody() {
        // 参数放在内容体里
        client.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=tester&userPassword=tester")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(3)
    public void testLogOutWhenNotLogIn() {
        client.post()
                .uri("/logout")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(4)
    public void testLogInStatusWhenNotLogIn() {
        client.get()
                .uri("/login/status")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(5)
    public void testUpdateUsernameWhenNotLogIn() {
        client.post()
                .uri("/account/username?newValue=tester")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(6)
    public void testUpdatePasswordWhenNotLogIn() {
        client.post()
                .uri("/account/password?oldValue=tester&newValue=tester")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(7)
    public void testRemoveAccountWhenNotLogIn() {
        client.delete()
                .uri("/account")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(8)
    public void testGetUserWhenNotLogIn() {
        client.get()
                .uri("/account")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(9)
    public void testRegisterWithInvalidUsername() {
        client.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=!#tester&userPassword=123")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(10)
    public void testRegisterShouldSuccess() {
        client.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=tester&userPassword=123")
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(11)
    public void testRegisterWithDuplicatedUsername() {
        client.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=tester&userPassword=tester")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(12)
    public void testRegisterWithAnotherValid() {
        client.post()
                .uri("/register?userName=guest&userPassword=guest")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(13)
    public void testLogInWithValidAccount() {
        client.post()
                .uri("/login?userName=tester&userPassword=123")
                .exchange()
                .expectAll(
                        resp -> resp.expectStatus().isOk(),
                        resp -> resp.expectCookie().value(sessionCookieName, s -> sessionId = s)
                );

    }

    @Test
    @Order(14)
    public void testGetUserWhenLogIn() {
        String body = client.get()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("tester"));
    }

    @Test
    @Order(15)
    public void testUpdateUsernameWithInvalidValue() {
        client.post()
                .uri("/account/username?newValue=#@$Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(16)
    public void testUpdateUsernameWithDuplicatedValue() {
        client.post()
                .uri("/account/username?newValue=guest").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(17)
    public void testUpdateUsernameShouldSuccess() {
        client.post()
                .uri("/account/username?newValue=David").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(18)
    public void testUpdatePasswordWithWrongOldValue() {
        client.post()
                .uri("/account/password?oldValue=qwer&newValue=abc").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(19)
    public void testUpdatePasswordShouldSuccess() {
        client.post()
                .uri("/account/password?oldValue=123&newValue=abc").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(20)
    public void testGetUserConsistent() {
        String body = client.get()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("David"));
    }

    @Test
    @Order(21)
    public void testLogOutShouldSuccess() {
        client.post()
                .uri("/logout").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(22)
    public void testLogInStatusWhenLogOut() {
        client.get()
                .uri("/login/status").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(23)
    public void testUpdateUsernameWhenLogout() {
        client.post()
                .uri("/account/username?newValue=Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(24)
    public void testUpdatePasswordWhenLogout() {
        client.post()
                .uri("/account/password?oldValue=abc&newValue=tester").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(25)
    public void testRemoveUserWhenLogout() {
        client.delete()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(26)
    public void testLogInWithNewAccount() {
        client.post()
                .uri("/login?userName=David&userPassword=abc").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(27)
    public void testRemoveUserWhenLogIn() {
        client.delete()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(28)
    public void testLogInStatusAfterRemoveUser() {
        client.get()
                .uri("/login/status").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(29)
    public void testUpdateUsernameAfterRemoveUser() {
        client.post()
                .uri("/account/username?newValue=Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(30)
    public void testUpdatePasswordAfterRemoveUser() {
        client.post()
                .uri("/account/password").cookie(sessionCookieName, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("oldValue=abc&newValue=tester")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(31)
    public void testRemoveAgainAfterRemove() {
        client.delete()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(32)
    public void testGetUserAfterRemoveUser() {
        client.get()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(33)
    public void testLogInAgainWithAnotherAccount() {
        client.post()
                .uri("/login").cookie(sessionCookieName, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=guest&userPassword=guest")
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(34)
    public void testGetUserAgain() throws JsonProcessingException {
        String body = client.get()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        userDto = new ObjectMapper().readValue(body, UserDto.class);
        Assertions.assertEquals("guest", userDto.getUserName());
    }

    @Test
    @Order(35)
    public void testUpdateUserRoleWithWrongUserName() {
        client.post()
                .uri("/account/role?userName=non-existing&newValue=" + Role.MARKETER)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(36)
    public void testUpdateUserRoleShouldFail() {
        client.post()
                .uri("/account/role?userName=guest&newValue=" + Role.MARKETER + "&userRole=" + Role.CUSTOMER)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(37)
    public void testUpdateUserRoleShouldSuccess() {
        client.post()
                .uri("/account/role?userName=guest&newValue=" + Role.MARKETER + "&userRole=" + Role.ADMIN)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(38)
    public void testGetUserAfterChangingRole() {
        String body = client.get()
                .uri("/account").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains(Role.MARKETER.toString()));
    }

    @Test
    @Order(39)
    public void testLogOutAgain() {
        client.post()
                .uri("/logout").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(40)
    public void testSearchUserShouldBeEmpty() {
        String body = client
                .get()
                .uri("/user/search?userName=tester").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertEquals("[]", body);
    }

    @Test
    @Order(41)
    public void testSearchUserShouldNotBeEmpty() {
        String body = client
                .get()
                .uri("/user/search?userName=guest").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertNotEquals("[]", body);
    }

    @Test
    @Order(42)
    public void testGetUserByIdFail() {
        client.get().uri("/user/-1").exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(43)
    public void testGetUserByIdSuccess() throws JsonProcessingException {
        String body = client
                .get().uri("/user/" + userDto.getUserId())
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        UserDto result = new ObjectMapper().readValue(body, UserDto.class);
        Assertions.assertEquals(result.getUserName(), userDto.getUserName());
    }

    @Test
    @Order(44)
    public void testSearchUserWithUserRoleExist() {
        String body = client.get().uri("/user/search?userRole=" + Role.MARKETER)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("guest"));
    }

    @Test
    @Order(45)
    public void testSearchUserWithUserRoleNotExist() {
        String body = client.get().uri("/user/search?userRole=" + Role.QA_SUPERVISOR)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertEquals("[]", body);
    }

}
