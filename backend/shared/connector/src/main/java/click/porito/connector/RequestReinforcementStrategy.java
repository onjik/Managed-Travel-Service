package click.porito.connector;


import org.springframework.http.HttpEntity;

public interface RequestReinforcementStrategy {

    void reinforce(HttpEntity<?> request);

}
