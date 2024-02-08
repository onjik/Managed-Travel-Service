package click.porito.connector;

import click.porito.common.trace.TraceConfigurationProperties;
import click.porito.common.trace.TraceContext;
import click.porito.common.trace.TraceContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

public class TraceContextReinforcementStrategy implements RequestReinforcementStrategy{
    private final String CORRELATION_ID_HEADER_NAME;

    public TraceContextReinforcementStrategy(TraceConfigurationProperties traceConfigurationProperties) {
        Assert.notNull(traceConfigurationProperties, "traceConfigurationProperties must not be null");
        Assert.isTrue(traceConfigurationProperties.enabled(), "traceConfigurationProperties must be enabled");
        this.CORRELATION_ID_HEADER_NAME = traceConfigurationProperties.headerName();
    }

    @Override
    public void reinforce(HttpEntity<?> request) {
        HttpHeaders headers = request.getHeaders();
        if (TraceContextHolder.hasContext()) {
            TraceContext context = TraceContextHolder.getContext();
            String correlationId = context.getCorrelationId().orElse(null);
            String jwtToken = context.getJwtToken().orElse(null);
            if (!headers.containsKey(CORRELATION_ID_HEADER_NAME) && correlationId != null) {
                headers.add(CORRELATION_ID_HEADER_NAME, correlationId);
            }
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION) && jwtToken != null) {
                headers.add(HttpHeaders.AUTHORIZATION, jwtToken);
            }
        }
    }
}
