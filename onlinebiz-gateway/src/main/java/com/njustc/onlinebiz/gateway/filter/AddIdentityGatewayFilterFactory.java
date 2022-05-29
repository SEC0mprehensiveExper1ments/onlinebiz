package com.njustc.onlinebiz.gateway.filter;

import com.njustc.onlinebiz.common.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.stream.Collectors;

// 这个过滤器除了检查用户角色是否符合权限外，还会增加用户ID作为请求的
// 一个默认额外参数。
@Slf4j
@Component
public class AddIdentityGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AddIdentityGatewayFilterFactory.Config> {

    private static final String USER_SERVICE_ID = "onlinebiz-user";

    private final WebClient webClient;

    private final LoadBalancerClientFactory clientFactory;

    AddIdentityGatewayFilterFactory(LoadBalancerClientFactory clientFactory) {
        super(Config.class);
        this.webClient = WebClient.create();
        this.clientFactory = clientFactory;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            // 获取发送请求的用户身份
            Mono<ResponseEntity<UserDto>> entityMono = requestAccount(exchange.getRequest());
            return entityMono.flatMap(entity -> {
                // 检查响应结果
                UserDto user;
                if (entity == null || !entity.getStatusCode().equals(HttpStatus.OK)) {
                    user = null;
                } else {
                    user = entity.getBody();
                }
                if (user != null) {
                    // 重新构造请求参数
                    StringBuilder query = new StringBuilder();
                    request.getQueryParams().forEach((name, value) -> {
                        // 参数值是个List
                        if (!name.equals("userId") && !name.equals("userRole")) {
                            query.append(name).append("=").append(value.get(0)).append("&");
                        }
                    });
                    // 添加用户ID和角色信息
                    query.append("userId=").append(user.getUserId().toString()).append("&");
                    query.append("userRole=").append(user.getUserRole().toString());
                    // 构造新的URI
                    URI newUri = UriComponentsBuilder.fromUri(request.getURI())
                            .replaceQuery(query.toString()).build(true).toUri();
                    // 构造新的request
                    ServerHttpRequest newRequest = request.mutate().uri(newUri).build();
                    // 构造新的exchange
                    ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

                    log.info("Request to: " + newExchange.getRequest().getURI());

                    return chain.filter(newExchange);
                }
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            });
        };
    }

    // 发送请求到用户服务获取当前用户身份
    private Mono<ResponseEntity<UserDto>> requestAccount(ServerHttpRequest request) {
        ReactorLoadBalancer<ServiceInstance> loadBalancer = clientFactory.getInstance(USER_SERVICE_ID,
                ReactorServiceInstanceLoadBalancer.class);
        return loadBalancer.choose().flatMap(response -> {
            if (!response.hasServer()) {
                return Mono.just(ResponseEntity.badRequest().build());
            }
            ServiceInstance server = response.getServer();
            String uri = "http://" + server.getHost() + ":" + server.getPort() + "/api/account";
            // 提取请求的cookie值
            MultiValueMap<String, String> cookieMap = new LinkedMultiValueMap<>();
            request.getCookies().forEach((name, list) ->
                    cookieMap.addAll(name, list.stream().map(HttpCookie::getValue).collect(Collectors.toList())));
            return webClient
                    .get().uri(uri).cookies(cookies -> cookies.addAll(cookieMap))
                    .retrieve().toEntity(UserDto.class);
        });
    }

    public static class Config {

    }

}
