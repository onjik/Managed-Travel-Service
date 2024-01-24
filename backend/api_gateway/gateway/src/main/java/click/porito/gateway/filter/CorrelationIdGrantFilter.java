package click.porito.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static click.porito.gateway.filter.CorrelationUtil.*;

@Slf4j
@Order(1)
@Component
public class CorrelationIdGrantFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(headers)) {
            log.debug("X-Correlation-Id found in tracking filter: {}. ", headers.get(CORRELATION_ID_HEADER_NAME));
        } else {
            String correlationId = generateCorrelationId();
            exchange = setCorrelationId(exchange, correlationId);
            log.debug("X-Correlation-Id generated in tracking filter: {}.", correlationId);
        }
        return chain.filter(exchange);
    }

}
