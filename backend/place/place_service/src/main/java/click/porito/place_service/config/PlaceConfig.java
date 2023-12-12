package click.porito.place_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
public class PlaceConfig {


    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @Bean
    public GoogleApiContext googleApiContext() {
        return new GoogleApiContext(GOOGLE_API_KEY);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
