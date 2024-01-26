package click.porito.travel_core_service.config;

import click.porito.common.autoconfigure.EnableDistributedRestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDistributedRestTemplate
public class LoadBalancerConfig {

}
