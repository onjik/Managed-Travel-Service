package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector;


import org.springframework.http.HttpEntity;

public interface RequestReinforcementStrategy {

    void reinforce(HttpEntity<?> request);

}
