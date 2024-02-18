package click.porito.managed_travel.porito_travel_spring_boot_starter_account_servlet_connector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "porito.connector.account")
public record AccountConnectorProperty(
        @DefaultValue("true")
        boolean enabled,
        @DefaultValue("http://account-service")
        String uriPrefix
) {
}
