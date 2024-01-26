package click.porito.common.autoconfigure;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Service Discovery, Client Side Load Balancing, Trace Interceptor를 적용합니다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@Import(DistributedRestTemplateAutoConfiguration.class)
public @interface EnableDistributedRestTemplate {
}
