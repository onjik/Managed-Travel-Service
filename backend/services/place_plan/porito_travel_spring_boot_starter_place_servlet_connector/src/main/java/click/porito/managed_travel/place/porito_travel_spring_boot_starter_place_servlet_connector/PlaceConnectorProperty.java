package click.porito.managed_travel.place.porito_travel_spring_boot_starter_place_servlet_connector;

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
