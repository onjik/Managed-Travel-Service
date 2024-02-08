package click.porito.api_gateway_service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class CorrelationIdResponseFilter implements GlobalFilter {
    private final String CORRELATION_ID_HEADER_NAME;

    public CorrelationIdResponseFilter(String correlationIdHeaderName) {
        CORRELATION_ID_HEADER_NAME = correlationIdHeaderName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.deferContextual(ctx -> {
                    String correlationId = ctx.get(CorrelationIdGrantFilter.CORRELATION_ID_CONTEXT);
                    HttpHeaders headers = exchange.getResponse().getHeaders();
                    // 기존 응답의 correlationId 는 삭제하고, 새로운 correlationId 를 추가한다.
                    headers.put(CORRELATION_ID_HEADER_NAME, List.of(correlationId));
                    log.debug("Correlation-Id added to response : {}.", correlationId);
                    return Mono.empty();
                }));
    }
}
