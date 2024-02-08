package click.porito.common.trace;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "porito.trace")
public record TraceConfigurationProperties(
        @DefaultValue("true")
        boolean enabled,
        @DefaultValue("X-Correlation-ID")
        String headerName
) {
}
