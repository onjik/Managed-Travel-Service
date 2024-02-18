package click.porito.connector;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.ConnectorBaseAutoConfiguration;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.RestExchangeable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectorBaseAutoConfigurationTest {

    ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ConnectorBaseAutoConfiguration.class));

    @Test
    void loadBalancedRestTemplateShouldBeCreated() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RestTemplate.class);
            assertThat(context).hasBean("loadBalancedRestTemplate");
        });
    }

    @Bean
    void restExchangeableShouldBeCreated() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RestExchangeable.class);
        });
    }

}