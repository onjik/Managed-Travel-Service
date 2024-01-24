package click.porito.gateway.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

public class CorrelationUtil {
    public static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";

    public static boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        return requestHeaders.get(CORRELATION_ID_HEADER_NAME) != null;
    }

    public static String getCorrelationId(HttpHeaders requestHeaders) {
        return requestHeaders.getFirst(CORRELATION_ID_HEADER_NAME);
    }

    public static ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header(CORRELATION_ID_HEADER_NAME, correlationId)
                                .build()
                )
                .build();
    }

    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

}
