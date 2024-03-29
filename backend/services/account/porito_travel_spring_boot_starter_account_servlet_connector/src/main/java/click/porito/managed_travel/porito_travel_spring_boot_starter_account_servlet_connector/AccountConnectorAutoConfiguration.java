package click.porito.managed_travel.porito_travel_spring_boot_starter_account_servlet_connector;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.RestExchangeable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(AccountConnectorProperty.class)
@ConditionalOnProperty(value = "porito.connector.account.enabled", havingValue = "true", matchIfMissing = true)
public class AccountConnectorAutoConfiguration {
    private final AccountConnectorProperty accountConnectorProperty;

    public AccountConnectorAutoConfiguration(AccountConnectorProperty accountConnectorProperty) {
        this.accountConnectorProperty = accountConnectorProperty;
    }

    @Bean
    @ConditionalOnMissingBean
    public AccountRestConnector accountRestConnector(RestExchangeable restExchangeable) {
        return new AccountRestConnector(restExchangeable, accountConnectorProperty.uriPrefix());
    }
}
