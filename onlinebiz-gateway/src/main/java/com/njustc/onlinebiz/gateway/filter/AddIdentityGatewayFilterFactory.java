package com.njustc.onlinebiz.gateway.filter;

import com.njustc.onlinebiz.common.model.User;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

// 这个过滤器除了检查用户角色是否符合权限外，还会增加用户ID作为请求的
// 一个默认额外参数。
@Component
public class AddIdentityGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AddIdentityGatewayFilterFactory.Config> {

    private static final String SESSIONID_TO_USER = "sessionId2user";

    private static final String SESSION_ID_COOKIE_NAME = "NJUSTC_ONLINEBIZ_SESSION";

    private final RedisTemplate<Object, Object> redisTemplate;

    public AddIdentityGatewayFilterFactory(RedisTemplate<Object, Object> redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            HttpCookie sessionIdCookie = request.getCookies().getFirst(SESSION_ID_COOKIE_NAME);
            if (sessionIdCookie != null) {
                User user = (User) redisTemplate.opsForHash().get(SESSIONID_TO_USER, sessionIdCookie.getValue());
                if (user != null) {
                    request.getQueryParams().remove("userId");
                    request.getQueryParams().remove("userRole");
                    request.getQueryParams().set("userId", user.getUserId().toString());
                    request.getQueryParams().set("userRole", user.getUserRole().toString());
                    return chain.filter(exchange);
                }
            }
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        };
    }

    public static class Config {

    }

}
