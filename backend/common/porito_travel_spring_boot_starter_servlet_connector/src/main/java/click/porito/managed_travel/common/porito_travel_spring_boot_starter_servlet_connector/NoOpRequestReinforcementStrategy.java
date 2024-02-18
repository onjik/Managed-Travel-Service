package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector;

import org.springframework.http.HttpEntity;

public class NoOpRequestReinforcementStrategy implements RequestReinforcementStrategy{
    @Override
    public void reinforce(HttpEntity<?> request) {
        // do nothing
    }
}
