package click.porito.place_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RestCallFailedException extends RuntimeException{
    private final String uri;
    private final HttpMethod httpMethod;
    private HttpEntity<?> requestEntity;
    private String message;
    private HttpStatusCode httpStatus;


}
