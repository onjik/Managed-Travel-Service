package click.porito.managed_travel.place.porito_travel_spring_boot_starter_place_servlet_connector;


import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.RestExchangeable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(PlaceConnectorProperty.class)
@ConditionalOnProperty(value = "porito.connector.place.enabled", havingValue = "true", matchIfMissing = true)
public class PlaceConnectorAutoConfiguration {
    private final PlaceConnectorProperty placeConnectorProperty;

    public PlaceConnectorAutoConfiguration(PlaceConnectorProperty placeConnectorProperty) {
        this.placeConnectorProperty = placeConnectorProperty;
    }

    @Bean
    @ConditionalOnMissingBean
    public PlaceRestConnector placeRestConnector(RestExchangeable restExchangeable) {
        return new PlaceRestConnector(restExchangeable, placeConnectorProperty.uriPrefix());
    }
}
