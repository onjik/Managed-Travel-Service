package click.porito.connector;

import click.porito.common.exception.ServerThrownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
public class DefaultRestExchangeable implements RestExchangeable{

    private final RestTemplate restTemplate;
    private final RequestReinforcementStrategy requestReinforcementStrategy;

    public DefaultRestExchangeable(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, RequestReinforcementStrategy requestReinforcementStrategy) {
        this.restTemplate = restTemplate;
        this.requestReinforcementStrategy = requestReinforcementStrategy;
    }

    @Override
    public <T> Optional<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws ServerThrownException {
        requestReinforcementStrategy.reinforce(requestEntity);
        return Optional.ofNullable(
                restTemplate.exchange(url, method, requestEntity, responseType, uriVariables).getBody()
        );
    }

    @Override
    public <T> Optional<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        requestReinforcementStrategy.reinforce(requestEntity);
        return Optional.ofNullable(
                restTemplate.exchange(url, method, requestEntity, responseType, uriVariables).getBody()
        );
    }

    @Override
    public <T> void doRequest(String url, HttpMethod method, HttpEntity<?> requestEntity, Object... uriVariables) {
        requestReinforcementStrategy.reinforce(requestEntity);
        restTemplate.exchange(url, method, requestEntity, Void.class, uriVariables);
    }


}
