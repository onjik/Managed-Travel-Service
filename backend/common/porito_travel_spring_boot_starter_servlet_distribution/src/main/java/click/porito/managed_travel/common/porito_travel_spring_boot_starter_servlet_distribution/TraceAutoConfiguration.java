package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_distribution;

import click.porito.common.trace.TraceContextStrategy;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TraceContextFilter.class)
    public TraceContextFilter traceContextFilter() {
        return new TraceContextFilter();
    }

    @Bean
    @ConditionalOnMissingBean(TraceContextStrategy.class)
    public TraceContextStrategy traceContextStrategy() {
        return new ThreadLocalTraceContextStrategy();
    }

    @Bean(name = "distributedRestTemplate")
    @LoadBalanced
    @ConditionalOnMissingBean(name = "distributedRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new TraceContextInterceptor());
        return restTemplate;
    }

}
