package click.porito.common.autoconfigure;

import click.porito.common.trace.TraceContextFilter;
import click.porito.common.trace.TraceContextInterceptor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;
@EnableDiscoveryClient
@Configuration(proxyBeanMethods = false)
public class DistributedRestTemplateAutoConfiguration {

    @Primary
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors()
                .add(new TraceContextInterceptor());
        return restTemplate;
    }

    @Order(1)
    @Bean
    public TraceContextFilter traceContextFilter() {
        return new TraceContextFilter();
    }
}
