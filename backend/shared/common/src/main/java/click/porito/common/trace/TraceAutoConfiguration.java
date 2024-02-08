package click.porito.common.trace;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
@EnableConfigurationProperties(TraceConfigurationProperties.class)
@ConditionalOnProperty(prefix = "porito.trace", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TraceAutoConfiguration {

    private final TraceConfigurationProperties traceConfigurationProperties;


    public TraceAutoConfiguration(TraceConfigurationProperties traceConfigurationProperties) {
        this.traceConfigurationProperties = traceConfigurationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(TraceContextFilter.class)
    public TraceContextFilter traceContextFilter() {
        return new TraceContextFilter(traceConfigurationProperties);
    }

}
