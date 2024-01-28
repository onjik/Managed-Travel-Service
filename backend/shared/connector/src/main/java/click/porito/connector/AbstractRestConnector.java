package click.porito.connector;

import click.porito.common.exception.*;
import click.porito.common.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRestConnector {
    private final RestTemplate restTemplate;

    @NonNull
    protected abstract Domain getDomain();

    protected <T> Optional<T> doExchange(Function<RestTemplate, ResponseEntity<T>> exchangeFunction) {
        try {
            return Optional.ofNullable(
                    exchangeFunction.apply(restTemplate).getBody()
            );
        } catch (Throwable e) {
            throw exceptionWrapper(e);
        }
    }

    protected void doRequest(Consumer<RestTemplate> exchangeConsumer){
        try {
            exchangeConsumer.accept(restTemplate);
        } catch (Throwable e) {
            throw exceptionWrapper(e);
        }
    }

    @NonNull
    private ServerThrownException exceptionWrapper(Throwable e){
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
