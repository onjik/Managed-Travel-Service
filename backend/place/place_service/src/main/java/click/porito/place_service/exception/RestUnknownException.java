package click.porito.place_service.exception;

import lombok.Builder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

public class RestUnknownException extends RestCallFailedException {

    @Builder
    public RestUnknownException(String uri, HttpMethod httpMethod, HttpEntity<?> requestEntity, String message, HttpStatusCode httpStatus) {
        super(uri, httpMethod, requestEntity, message, httpStatus);
    }
}
