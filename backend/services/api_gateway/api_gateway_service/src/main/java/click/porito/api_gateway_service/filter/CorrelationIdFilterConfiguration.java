package click.porito.api_gateway_service.filter;

import click.porito.common.trace.TraceConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@EnableConfigurationProperties(TraceConfigurationProperties.class)
@Configuration
public class CorrelationIdFilterConfiguration {

    private final String CORRELATION_ID_HEADER_NAME;

    public CorrelationIdFilterConfiguration(TraceConfigurationProperties traceConfigurationProperties) {
        this.CORRELATION_ID_HEADER_NAME = traceConfigurationProperties.headerName();
    }

    @Bean
    @Order(1)
    public CorrelationIdGrantFilter correlationIdGrantFilter() {
        return new CorrelationIdGrantFilter(CORRELATION_ID_HEADER_NAME);
    }

    @Bean
    @Order(2)
    public CorrelationIdResponseFilter correlationIdResponseFilter() {
        return new CorrelationIdResponseFilter(CORRELATION_ID_HEADER_NAME);
    }
}
