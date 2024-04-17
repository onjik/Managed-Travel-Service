package click.porito.managed_travel.place.place_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(order = 0) // method security 보다 먼저 실행되도록
public class TransactionConfig {
}
