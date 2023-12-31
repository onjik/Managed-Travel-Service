package click.porito.place_service.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "place.cache")
public class PlaceCacheConfiguration {
    private long durationSeconds = 10 * 60 * 24; // 10 days
}
