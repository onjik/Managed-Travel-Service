package click.porito.connector;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;
import click.porito.common.exception.ErrorResponseBody;
import click.porito.common.exception.ServerThrownException;
import click.porito.common.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRestConnector implements RestExchangeable {
    private final RestExchangeable restExchangeable;
    private final String uriPrefix;

    protected abstract Domain getDomain();

    @Override
    public <T> Optional<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables){
        try {
            return restExchangeable.doExchange(uriPrefix + url, method, requestEntity, responseType, uriVariables);
        } catch (Throwable e) {
            throw exceptionWrapper(e);
        }
    }


    @Override
    public <T> Optional<T> doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        try {
            return restExchangeable.doExchange(uriPrefix + url, method, requestEntity, responseType, uriVariables);
        } catch (Throwable e) {
            throw exceptionWrapper(e);
        }
    }

    @Override
    public <T> void doRequest(String url, HttpMethod method, HttpEntity<?> requestEntity, Object... uriVariables) {
        try {
            restExchangeable.doRequest(uriPrefix + url, method, requestEntity, uriVariables);
        } catch (Throwable e) {
            throw exceptionWrapper(e);
        }
    }

    @NonNull
    protected ServerThrownException exceptionWrapper(Throwable e){
        if (e instanceof RestClientResponseException) {
            log.debug("error occurred while parse errorResponse from http response", e);
            HttpStatusCode statusCode = ((RestClientResponseException) e).getStatusCode();
            Optional<ErrorResponseBody> errorResponseBody = parseErrorResponseBody((RestClientResponseException) e);
            final ErrorCode errorCode;
            if (errorResponseBody.isPresent()) {
                ErrorResponseBody body = errorResponseBody.get();
                errorCode = ErrorCodeMapper.of(body);
            } else {
                errorCode = new StatusOnlyErrorCode(statusCode);
            }

            if (statusCode.is4xxClientError()) {
                return new ConnectorBusinessException(e,getDomain(), errorCode);
            } else {
                return new ConnectorServerException(e, getDomain(), errorCode);
            }
        } else {
            log.error("unexpected error occurred while request to server (domain: {})", getDomain(), e);
            return new ConnectorServerException(e, getDomain(), ErrorCodes.UNEXPECTED_SERVER_ERROR);
        }

    }

    private Optional<ErrorResponseBody> parseErrorResponseBody(RestClientResponseException e) {
        try {
            ErrorResponseBody body = e.getResponseBodyAs(ErrorResponseBody.class);
            return Optional.ofNullable(body);
        } catch (Exception ex) {
            log.debug("error occurred while parse errorResponse from http response", ex);
            return Optional.empty();
        }
    }

}
