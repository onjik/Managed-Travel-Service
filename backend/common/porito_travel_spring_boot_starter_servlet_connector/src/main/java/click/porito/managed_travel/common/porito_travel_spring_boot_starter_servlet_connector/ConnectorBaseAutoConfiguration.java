package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.cloud.client.loadbalancer.LoadBalanced")
@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled", matchIfMissing = true)
public class ConnectorBaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestExchangeable restExchangeable(@Qualifier("distributedRestTemplate") RestTemplate restTemplate) {
        RequestReinforcementStrategy strategy = new NoOpRequestReinforcementStrategy();
        return new DefaultRestExchangeable(restTemplate, strategy);
    }

}
