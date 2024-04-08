package click.porito.place_connector;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.ConnectorBaseAutoConfiguration;
import click.porito.managed_travel.place.porito_travel_spring_boot_starter_place_servlet_connector.PlaceConnectorAutoConfiguration;
import click.porito.managed_travel.place.porito_travel_spring_boot_starter_place_servlet_connector.PlaceRestConnector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceViewConnectorAutoConfigurationTest {

    ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(PlaceConnectorAutoConfiguration.class, ConnectorBaseAutoConfiguration.class));

    @Test
    void placeRestConnector() {
        applicationContextRunner.run(context -> {
            assertThat(context).hasSingleBean(PlaceRestConnector.class);
        });
    }

}