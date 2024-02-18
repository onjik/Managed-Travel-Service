package click.porito.plan_connector;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.ConnectorBaseAutoConfiguration;
import click.porito.managed_travel.plan.plan_connector.PlanConnectorAutoConfiguration;
import click.porito.managed_travel.plan.plan_connector.PlanRestConnector;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PlanConnectorAutoConfigurationTest {

    ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(PlanConnectorAutoConfiguration.class, ConnectorBaseAutoConfiguration.class));

    @Test
    void planRestConnector() {
        applicationContextRunner.run(context -> {
            assertThat(context).hasSingleBean(PlanRestConnector.class);
        });
    }

}