package click.porito.managed_travel.plan.plan_connector;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.RestExchangeable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(PlanConnectorProperty.class)
@ConditionalOnProperty(value = "porito.connector.plan.enabled", havingValue = "true", matchIfMissing = true)
public class PlanConnectorAutoConfiguration {
    private final PlanConnectorProperty planConnectorProperty;

    public PlanConnectorAutoConfiguration(PlanConnectorProperty planConnectorProperty) {
        this.planConnectorProperty = planConnectorProperty;
    }

    @Bean
    @ConditionalOnMissingBean
    public PlanRestConnector planRestConnector(RestExchangeable restExchangeable) {
        return new PlanRestConnector(restExchangeable, planConnectorProperty.uriPrefix());
    }
}
