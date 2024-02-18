package click.porito.common.trace;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TraceContextFilterTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TraceAutoConfiguration.class));

    @Test
    void test() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(TraceContextFilter.class);
        });
    }

}