package click.porito.account_service.config;

import click.porito.common.autoconfigure.EnableDistributedRestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDistributedRestTemplate
//@EnableDiscoveryClient - 위에 포함되어 있음
public class ServiceDiscoveryConfig {
}
