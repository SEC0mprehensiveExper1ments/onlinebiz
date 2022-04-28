package com.njustc.onlinebiz.user;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

// 在这里我们要启动整个应用，然后通过 WebTestClient 的实例向 Controller
// 发送请求并检验响应状态。由于我们不是在浏览器环境下测试，所以我们需要手动
// 为后续每个请求加上 cookie，并且由于每个测试用例使用一个不同的测试类实例，
// 我们需要把测试间共享的数据放在静态域中。
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIT {

    // 用来发送请求的对象
    private static final WebTestClient client = WebTestClient.bindToServer().baseUrl("http://124.222.168.27:8080").build();

    // 从配置文件中读取 cookie 的名称
    private static final String sessionCookieName = "NJUSTC_ONLINEBIZ_SESSION";

    // 保存会话 id 用于后面的测试
    private static String sessionId;

    @Test
    @Order(1)
    public void testLogInWithFakeAccountInURL() {
        // 参数放在 url 上
        client.post()
                .uri("/login?userName=admin&userPassword=admin")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(2)
    public void testLogInWithFakeAccountInBody() {
        // 参数放在内容体里
        client.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=admin&userPassword=admin")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(3)
    public void testLogOutWithoutLogIn() {
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
                .uri("/user/username?newValue=admin")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(6)
    public void testUpdatePasswordWhenNotLogIn() {
        client.post()
                .uri("/user/password?oldValue=admin&newValue=admin")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(7)
    public void testRemoveAccountWhenNotLogIn() {
        client.delete()
                .uri("/user")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(8)
    public void testGetUserIdentityWhenNotLogIn() {
        client.get()
                .uri("/user/identity")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(9)
    public void testRegisterWithInvalidUsername() {
        client.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=!#admin&userPassword=123")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(10)
    public void testRegisterShouldSuccess() {
        client.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=admin&userPassword=123")
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(11)
    public void testRegisterWithDuplicatedUsername() {
        client.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=admin&userPassword=admin")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(12)
    public void testRegisterWithAnotherValid() {
        client.post()
                .uri("/register?userName=guest&userPassword=guest")
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(13)
    public void testLogInWithValidAccount() {
        client.post()
                .uri("/login?userName=admin&userPassword=123")
                .exchange()
//                .expectStatus().isOk();
                .expectAll(
                        resp -> resp.expectStatus().isOk(),
                        resp -> resp.expectCookie().value(sessionCookieName, s -> sessionId = s)
                );
    }

    @Test
    @Order(14)
    public void testLogInStatusWhenLogIn() {
        client.get()
                .uri("/login/status").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(15)
    public void testUserExistenceWithExistingUsername() {
        client.get()
                .uri("/user/existence?userName=admin").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(16)
    public void testUserExistenceWithNonExistingUsername() {
        client.get()
                .uri("/user/existence?userName=Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(17)
    public void testGetUserIdentityWhenLogIn() {
        String body = client.get()
                .uri("/user/identity").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("admin"));
    }

    @Test
    @Order(18)
    public void testUpdateUsernameWithInvalidValue() {
        client.post()
                .uri("/user/username?newValue=#@$Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(19)
    public void testUpdateUsernameWithDuplicatedValue() {
        client.post()
                .uri("/user/username?newValue=guest").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(20)
    public void testUpdateUsernameShouldSuccess() {
        client.post()
                .uri("/user/username?newValue=David").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(21)
    public void testUpdatePasswordWithWrongOldValue() {
        client.post()
                .uri("/user/password?oldValue=qwer&newValue=abc").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(22)
    public void testUpdatePasswordShouldSuccess() {
        client.post()
                .uri("/user/password?oldValue=123&newValue=abc").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(23)
    public void testUserIdentityConsistent() {
        String body = client.get()
                .uri("/user/identity").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("David"));
    }

    @Test
    @Order(24)
    public void testLogOutShouldSuccess() {
        client.post()
                .uri("/logout").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(25)
    public void testLogInStatusWhenLogOut() {
        client.get()
                .uri("/login/status").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(26)
    public void testUpdateUsernameWhenLogout() {
        client.post()
                .uri("/user/username?newValue=Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(27)
    public void testUpdatePasswordWhenLogout() {
        client.post()
                .uri("/user/password?oldValue=abc&newValue=admin").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(28)
    public void testRemoveUserWhenLogout() {
        client.delete()
                .uri("/user").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(29)
    public void testLogInWithNewAccount() {
        client.post()
                .uri("/login?userName=David&userPassword=abc").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(30)
    public void testRemoveUserWhenLogIn() {
        client.delete()
                .uri("/user").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(31)
    public void testLogInStatusAfterRemoveUser() {
        client.get()
                .uri("/login/status").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(32)
    public void testUpdateUsernameAfterRemoveUser() {
        client.post()
                .uri("/user/username?newValue=Jack").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(33)
    public void testUpdatePasswordAfterRemoveUser() {
        client.post()
                .uri("/user/password").cookie(sessionCookieName, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("oldValue=abc&newValue=admin")
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(34)
    public void testRemoveAgainAfterRemove() {
        client.delete()
                .uri("/user").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(35)
    public void testUserExistenceAfterRemoveUser() {
        client.get()
                .uri("/user/existence?userName=admin").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
        client.get()
                .uri("/user/existence?userName=guest").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(36)
    public void testGetUserIdentityAfterRemoveUser() {
        client.get()
                .uri("/user/identity").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    @Order(37)
    public void testLogInAgainWithAnotherAccount() {
        client.post()
                .uri("/login").cookie(sessionCookieName, sessionId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("userName=guest&userPassword=guest")
                .exchange().expectStatus().isOk();
    }

    @Test
    @Order(38)
    public void testUserIdentityAgain() {
        String body = client.get()
                .uri("/user/identity").cookie(sessionCookieName, sessionId)
                .exchange().returnResult(String.class).getResponseBody().blockFirst();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("guest"));
    }

    @Test
    @Order(39)
    public void testLogOutAgain() {
        client.post()
                .uri("/logout").cookie(sessionCookieName, sessionId)
                .exchange().expectStatus().isOk();
    }
}
