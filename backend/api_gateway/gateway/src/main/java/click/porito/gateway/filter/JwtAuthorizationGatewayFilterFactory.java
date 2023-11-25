package click.porito.gateway.filter;

import click.porito.gateway.service.JwtPayLoad;
import click.porito.gateway.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static click.porito.gateway.constant.CustomHeaders.X_USER_ID;
import static click.porito.gateway.constant.CustomHeaders.X_USER_ROLES;

@Component
@Slf4j
public class JwtAuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthorizationGatewayFilterFactory.Config> {
    public final static Integer ORDER = 1;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationGatewayFilterFactory(JwtService jwtService, ObjectMapper objectMapper) {
        super(Config.class);
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return super.shortcutFieldOrder();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String token = extractToken(request);
            if (token == null) {
                return onError(response, "No token", HttpStatus.UNAUTHORIZED);
            }

            JwtPayLoad jwtPayLoad = jwtService.decode(token);
            if (!hasRoles(jwtPayLoad, config.allowedRoles)) {
                return onError(response, "No permission", HttpStatus.FORBIDDEN);
            }

            addAuthorizationHeaders(request, jwtPayLoad);
            return chain.filter(exchange);
        },ORDER);
    }

    private String extractToken(ServerHttpRequest request) {
        String rawHeaderValue = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (rawHeaderValue == null || rawHeaderValue.isBlank()) {
            return null;
        }
        return rawHeaderValue.replace("Bearer", "").trim();
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    private boolean hasRoles(JwtPayLoad jwtPayLoad, String[] allowedRoles) {
        for (String allowedRole : allowedRoles) {
            if (jwtPayLoad.roles().contains(allowedRole)) {
                return true;
            }
        }
        return false;
    }

    private void addAuthorizationHeaders(ServerHttpRequest request, JwtPayLoad jwtPayLoad) {
        var type = new TypeReference<>() {};
        final String rolesJson;
        try {
            rolesJson = objectMapper.writeValueAsString(jwtPayLoad.roles());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("failed to serialize roles", e);
        }
        request.mutate()
                .header(X_USER_ID, jwtPayLoad.userId())
                .header(X_USER_ROLES, rolesJson)
                .build();
    }

    @Setter
    @AllArgsConstructor
    public static class Config{
        String[] allowedRoles;
    }
}
