package click.porito.travel_plan_service.event_listener;

import click.porito.travel_plan_service.AccountTopics;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaEventListener {

    @KafkaListener(topics = AccountTopics.ACCOUNT_PUT_0)
    public void listenAccountPut(String message) {
        System.out.println(message);
    }
}
