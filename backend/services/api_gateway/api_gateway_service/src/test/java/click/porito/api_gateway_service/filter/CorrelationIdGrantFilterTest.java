package click.porito.api_gateway_service.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CorrelationIdGrantFilterTest {

    private final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";

    // 헤더에 생성 되었는지
    // 콘텍스트에 생성 되었는지
    @Test
    @DisplayName("Request Header에 Correlation-Id가 생성되어야 한다.")
    void mutateRequest() {
        // given
        CorrelationIdGrantFilter filter = new CorrelationIdGrantFilter(CORRELATION_ID_HEADER_NAME);
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080")
                .build();
        ServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();

        GatewayFilterChain chain = (ex) -> Mono.empty();
        // when
        filter.filter(exchange, chain).block();
        // then
        String first = request.getHeaders().getFirst(CORRELATION_ID_HEADER_NAME);
        Assertions.assertThat(first).isNotNull();
    }

    @Test
    @DisplayName("Request Context에 Correlation-Id가 생성되어야 한다.")
    void contextWrite() {
        // given
        CorrelationIdGrantFilter filter = new CorrelationIdGrantFilter(CORRELATION_ID_HEADER_NAME);
        MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080")
                .build();
        ServerWebExchange exchange = new MockServerWebExchange.Builder(request).build();

        GatewayFilterChain chain = (ex) -> Mono.empty();
        // when
        StepVerifier.create(filter.filter(exchange, chain))
                .expectAccessibleContext()
                .contains(CorrelationIdGrantFilter.CORRELATION_ID_CONTEXT, request.getHeaders().getFirst(CORRELATION_ID_HEADER_NAME))
                .then()
                .verifyComplete();
    }


}