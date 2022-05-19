package com.njustc.onlinebiz.gateway.filter;

import com.njustc.onlinebiz.common.model.User;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

// 这个过滤器除了检查用户角色是否符合权限外，还会增加用户ID作为请求的
// 一个默认额外参数。
@Component
public class CheckRoleGatewayFilterFactory
        extends AbstractGatewayFilterFactory<CheckRoleGatewayFilterFactory.Config> {

    private static final String SESSIONID_TO_USER = "sessionId2user";

    private static final String SESSION_ID_COOKIE_NAME = "NJUSTC_ONLINEBIZ_SESSION";

    private final RedisTemplate<Object, Object> redisTemplate;

    public CheckRoleGatewayFilterFactory(RedisTemplate<Object, Object> redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();
                HttpCookie sessionIdCookie = request.getCookies().getFirst(SESSION_ID_COOKIE_NAME);
                if (sessionIdCookie != null) {
                    User user = (User) redisTemplate.opsForHash().get(SESSIONID_TO_USER, sessionIdCookie.getValue());
                    if (user != null && config.getRoles().contains(user.getUserRole())) {
                        List<String> ids = request.getQueryParams().get("userId");
                        if (ids == null) {
                            request.getQueryParams().put("userId", List.of(user.getUserId().toString()));
                            return chain.filter(exchange);
                        } else if (ids.size() == 1 && ids.get(0).equals(user.getUserId().toString())) {
                            return chain.filter(exchange);
                        }
                    }
                }
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }

            @Override
            public String toString() {
                return filterToStringCreator(CheckRoleGatewayFilterFactory.this).append("roles", config.getRoles())
                        .toString();
            }
        };
    }

    public static class Config {

        private List<String> roles = List.of(Role.CUSTOMER);

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

    }

}
