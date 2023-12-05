package click.porito.travel_plan_service.place.google_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GoogleClientConfig {


    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @Bean
    public GoogleApiContext googleApiContext() {
        return new GoogleApiContext(GOOGLE_API_KEY);
    }


}
