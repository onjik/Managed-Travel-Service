package click.porito.travel_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableConfigurationProperties
@EnableDiscoveryClient
@SpringBootApplication
public class TravelCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelCoreApplication.class, args);
    }

}
