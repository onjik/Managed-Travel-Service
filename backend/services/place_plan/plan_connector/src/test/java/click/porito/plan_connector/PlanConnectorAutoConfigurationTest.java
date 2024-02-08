package click.porito.plan_connector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PlanConnectorAutoConfigurationTest {

    ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(PlanConnectorAutoConfiguration.class, click.porito.connector.LoadBalancedRestTemplateAutoConfiguration.class));

    @Test
    void planRestConnector() {
        applicationContextRunner.run(context -> {
            assertThat(context).hasSingleBean(PlanRestConnector.class);
        });
    }

}