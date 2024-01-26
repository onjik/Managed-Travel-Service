package click.porito.security.autoconfigure;

import click.porito.security.jwt_authentication.JwtConfigProperties;
import click.porito.security.jwt_authentication.JwtOperation;
import click.porito.security.jwt_authentication.JwtOperationImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JwtConfigProperties.class)
public class JwtOperationAutoConfiguration {

    @Bean
    public JwtOperation jwtOperation(JwtConfigProperties jwtConfigProperties) {
        log.info("load JwtOperation with AutoConfiguration");
        return new JwtOperationImpl(jwtConfigProperties);
    }
}
