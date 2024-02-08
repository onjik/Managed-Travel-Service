package click.porito.connector;

import org.springframework.http.HttpEntity;

public class NoOpRequestReinforcementStrategy implements RequestReinforcementStrategy{
    @Override
    public void reinforce(HttpEntity<?> request) {
        // do nothing
    }
}
