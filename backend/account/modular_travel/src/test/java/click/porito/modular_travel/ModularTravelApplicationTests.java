package click.porito.modular_travel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@EmbeddedKafka
@ActiveProfiles("test")
@SpringBootTest
class ModularTravelApplicationTests {

	@Test
	void contextLoads() {
	}

}
