package click.porito.place_service.external_api.google_api;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "google.api")
public final class GoogleApiContext {
    @NotNull
    private String key;
}
