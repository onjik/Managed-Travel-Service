package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication.JwtConfigProperties;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication.JwtOperation;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication.JwtOperationImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(JwtConfigProperties.class)
public class JwtOperationAutoConfiguration {

    @Bean(name = "jwtOperation")
    @ConditionalOnMissingBean
    public JwtOperation jwtOperation(JwtConfigProperties jwtConfigProperties) {
        return new JwtOperationImpl(jwtConfigProperties);
    }
}
