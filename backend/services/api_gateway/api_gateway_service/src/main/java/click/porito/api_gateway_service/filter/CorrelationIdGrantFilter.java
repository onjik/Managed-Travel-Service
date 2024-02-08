package click.porito.api_gateway_service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
public class CorrelationIdGrantFilter implements GlobalFilter {
    private final String CORRELATION_ID_HEADER_NAME;
    public static final String CORRELATION_ID_CONTEXT = "porito-correlation-id.context.key";

    public CorrelationIdGrantFilter(String CORRELATION_ID_HEADER_NAME) {
        this.CORRELATION_ID_HEADER_NAME = CORRELATION_ID_HEADER_NAME;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String correlationId = UUID.randomUUID().toString();
        exchange = exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header(CORRELATION_ID_HEADER_NAME, correlationId)
                                .build()
                )
                .build();
        log.debug("Correlation-Id generated : {}.", correlationId);
        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(CORRELATION_ID_CONTEXT, correlationId));
    }

}
