package click.porito.modular_travel.place.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleApiConfig {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @Bean
    public GoogleApiContext googleApiContext() {
        return new GoogleApiContext(GOOGLE_API_KEY);
    }
}
