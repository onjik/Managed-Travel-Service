package click.porito.connector;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface RestExchangeable {
    /**
     * 예시 코드
     * <pre>
     *     doExchange("http://test-server/accounts/{userId}", HttpMethod.GET, null, Account.class, userId);
     * </pre>
     */
    <T> Optional<T> doExchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables);
    <T> Optional<T> doExchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables);

    /**
     * 예시 코드
     * <pre>
     *     doRequest("http://test-server/accounts/{userId}", HttpMethod.PATCH, new HttpEntity<>(body), Void.class, userId);
     * </pre>
     */
    <T> void doRequest(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Object... uriVariables);

}
