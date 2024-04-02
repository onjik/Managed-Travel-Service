package click.porito.managed_travel.place.place_service.api.command.adapter.graphql;

import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.api.command.PlaceApi;
import click.porito.managed_travel.place.domain.api.request.NearBySearchQuery;
import click.porito.managed_travel.place.domain.exception.PlaceApiFailedException;
import graphql.GraphQLError;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlaceGraphqlApi {
    private final PlaceApi placeApi;

    @QueryMapping(name = "place")
    public Place place(@Argument("id") String id) {
        return placeApi.getPlace(id)
                .orElse(null);
    }

    @QueryMapping(name = "nearbyPlaces")
    public List<Place> nearbyPlaces(@Argument("input") @Valid NearBySearchQuery input) {
        return placeApi.getNearbyPlaces(input);
    }

    @QueryMapping(name = "photoUri")
    public String imageUri(@Argument("placeId") String placeId, @Argument("photoId") String photoId, @Argument("maxWidthPx") Integer maxWidthPx, @Argument("maxHeightPx") Integer maxHeightPx) {
        if (maxWidthPx == null) {
            maxWidthPx = 1920;
        }
        if (maxHeightPx == null) {
            maxHeightPx = 1080;
        }
        return placeApi.getPhotoUrl(placeId, photoId, maxWidthPx, maxHeightPx).orElse(null);
    }
    @GraphQlExceptionHandler(PlaceApiFailedException.class)
    public GraphQLError handleExternalApiException(PlaceApiFailedException e) {
        ErrorType errorType = ErrorType.BAD_REQUEST;
        return GraphQLError.newError()
                .errorType(errorType)
                .message(e.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(ConstraintViolationException.class)
    public GraphQLError handleConstraintViolationException(ConstraintViolationException e) {
        String violation = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " : " + v.getMessage())
                .collect(Collectors.joining(", "));
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(violation)
                .build();
    }



}
