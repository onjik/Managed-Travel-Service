package click.porito.security.autoconfigure;

import click.porito.security.jwt_authentication.JwtConfigProperties;
import click.porito.security.jwt_authentication.JwtOperation;
import click.porito.security.jwt_authentication.JwtOperationImpl;
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
