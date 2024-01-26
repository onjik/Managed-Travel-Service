package click.porito.travel_core_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class TravelCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelCoreApplication.class, args);
    }

}
