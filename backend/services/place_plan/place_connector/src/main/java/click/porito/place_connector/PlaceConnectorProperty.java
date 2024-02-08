package click.porito.place_connector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "porito.connector.place")
public record PlaceConnectorProperty(
        @DefaultValue("true")
        boolean enabled,
        @DefaultValue("http://travel-core")
        String uriPrefix
) {
}
