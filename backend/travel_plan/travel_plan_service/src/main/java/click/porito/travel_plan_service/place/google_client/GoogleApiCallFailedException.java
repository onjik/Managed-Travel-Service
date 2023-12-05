package click.porito.travel_plan_service.place.google_client;

import click.porito.travel_plan_service.place.AbstractPlaceFetchingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
public class GoogleApiCallFailedException extends AbstractPlaceFetchingException {
    private final HttpStatusCode statusCode;
    private final String message;


}
