package click.porito.account_connector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class AccountConnectorAutoConfigurationTest {

    ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AccountConnectorAutoConfiguration.class, click.porito.connector.LoadBalancedRestTemplateAutoConfiguration.class));

    @Test
    void accountRestConnector() {
        applicationContextRunner.run(context -> {
            assertThat(context).hasSingleBean(AccountRestConnector.class);
        });
    }

}