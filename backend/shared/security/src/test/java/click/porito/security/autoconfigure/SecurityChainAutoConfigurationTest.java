package click.porito.security.autoconfigure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityChainAutoConfigurationTest {

    @Test
    @DisplayName("SecurityFilterChain 이 없을 경우, chain 이 등록된다.")
    void testSecurityFilterChain() {
        // given
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withPropertyValues("jwt.secret=secret")
                .withConfiguration(AutoConfigurations.of(SecurityChainAutoConfiguration.class, JwtOperationAutoConfiguration.class));

        // when
        contextRunner.run(context -> {
            // then
            assertThat(context).hasSingleBean(SecurityFilterChain.class);
        });

    }

    @Test
    @DisplayName("SecurityFilterChain 이 이미 있을 경우, 등록되지 않는다.")
    void testSecurityFilterChainAlreadyExists() {
        // given
        SecurityFilterChain securityFilterChain = new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE, Collections.emptyList());
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(SecurityChainAutoConfiguration.class, JwtOperationAutoConfiguration.class))
                .withBean(SecurityFilterChain.class, () -> securityFilterChain)
                .withPropertyValues("jwt.secret=secret");

        // when
        contextRunner.run(context -> {
            // then
            assertThat(context).hasSingleBean(SecurityFilterChain.class);
        });

    }

}