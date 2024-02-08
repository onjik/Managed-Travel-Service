package click.porito.place_connector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceConnectorAutoConfigurationTest {

    ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(PlaceConnectorAutoConfiguration.class, click.porito.connector.LoadBalancedRestTemplateAutoConfiguration.class));

    @Test
    void placeRestConnector() {
        applicationContextRunner.run(context -> {
            assertThat(context).hasSingleBean(PlaceRestConnector.class);
        });
    }

}