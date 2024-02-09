package click.porito.security.autoconfigure;

import click.porito.security.jwt_authentication.JwtOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class JwtOperationAutoConfigurationTest {

    @Test
    @DisplayName("jwt.secret가 주어지면 JwtOperation이 생성된다.")
    void testJwtOperationAutoConfiguration() {
        // given
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withPropertyValues("jwt.secret=test-secret")
                .withUserConfiguration(JwtOperationAutoConfiguration.class);

        // when, then
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(JwtOperationAutoConfiguration.class);
            assertThat(context).hasSingleBean(JwtOperation.class);
        });
    }

    @Test
    @DisplayName("jwt.secret가 없으면 예외가 발생한다.")
    void testJwtOperationAutoConfigurationWhenJwtSecretIsNotSet() {
        // given
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withUserConfiguration(JwtOperationAutoConfiguration.class);

        // when, then
        contextRunner.run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure()).hasMessageContaining("jwt.secret is required");
        });
    }


}