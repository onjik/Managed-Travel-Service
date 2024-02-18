package click.porito.connector;

import click.porito.common.trace.TraceConfigurationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
@EnableDiscoveryClient
@EnableConfigurationProperties(TraceConfigurationProperties.class)
@ConditionalOnClass(name = "org.springframework.cloud.client.loadbalancer.LoadBalanced")
@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled", matchIfMissing = true)
public class LoadBalancedRestTemplateAutoConfiguration {

    private final TraceConfigurationProperties traceConfigurationProperties;

    public LoadBalancedRestTemplateAutoConfiguration(TraceConfigurationProperties traceConfigurationProperties) {
        this.traceConfigurationProperties = traceConfigurationProperties;
    }


    @LoadBalanced
    @Bean(name = "loadBalancedRestTemplate")
    @ConditionalOnMissingBean(name = "loadBalancedRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestExchangeable restExchangeable(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        RequestReinforcementStrategy strategy;
        if (traceConfigurationProperties.enabled()) {
            strategy = new TraceContextReinforcementStrategy(traceConfigurationProperties);
        } else {
            strategy = new NoOpRequestReinforcementStrategy();
        }

        return new DefaultRestExchangeable(restTemplate, strategy);
    }

}
