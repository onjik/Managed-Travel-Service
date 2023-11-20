package click.porito.modular_travel.place.exception;

import lombok.Builder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;

public class RestServerErrorException extends RestCallFailedException{
    @Builder
    public RestServerErrorException(@NonNull String uri, @NonNull HttpMethod httpMethod, HttpEntity<?> requestEntity, String message, HttpStatusCode httpStatus) {
        super(uri, httpMethod, requestEntity, message, httpStatus);
    }
}
