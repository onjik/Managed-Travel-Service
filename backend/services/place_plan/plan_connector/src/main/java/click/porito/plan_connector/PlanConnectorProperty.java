package click.porito.plan_connector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "porito.connector.plan")
public record PlanConnectorProperty(
        @DefaultValue("true")
        boolean enabled,
        @DefaultValue("http://travel-core")
        String uriPrefix
) {
}
