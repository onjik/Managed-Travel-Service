package click.porito.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@EmbeddedKafka
@ActiveProfiles("test")
@SpringBootTest
class AccountApplicationTests {

	@Test
	void contextLoads() {
	}

}
