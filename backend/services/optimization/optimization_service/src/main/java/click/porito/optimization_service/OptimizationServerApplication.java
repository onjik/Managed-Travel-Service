package click.porito.optimization_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableConfigurationProperties
@EnableDiscoveryClient
@SpringBootApplication
public class OptimizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OptimizationServerApplication.class, args);
    }

}
