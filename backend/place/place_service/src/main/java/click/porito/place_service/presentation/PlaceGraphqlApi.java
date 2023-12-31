package click.porito.place_service.presentation;

import click.porito.place_service.PhotoService;
import click.porito.place_service.PlaceService;
import click.porito.place_service.PlaceType;
import click.porito.place_service.external_api.ExternalApiException;
import click.porito.place_service.model.PlaceDto;
import graphql.GraphQLError;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
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
    private final PhotoService photoService;
    private final PlaceService placeService;

    @QueryMapping(name = "place")
    public PlaceDto place(@Argument("id") String id) {
        return placeService.getPlace(id);
    }

    @QueryMapping(name = "nearbyPlaces")
    public List<PlaceDto> nearbyPlaces(@Argument("input") @Valid NearByPlacesInput input) {
        return placeService.getNearbyPlaces(
                input.latitude(),
                input.longitude(),
                input.radiusMeters(),
                input.maxResultCount(),
                input.placeTypes(),
                input.distanceSort()
        );
    }

    @QueryMapping(name = "photoUri")
    public String imageUri(@Argument("placeId") String placeId, @Argument("photoId") String photoId, @Argument("maxWidthPx") Integer maxWidthPx, @Argument("maxHeightPx") Integer maxHeightPx) {
        if (maxWidthPx == null) {
            maxWidthPx = 1920;
        }
        if (maxHeightPx == null) {
            maxHeightPx = 1080;
        }
        return photoService.getPhotoUrl(placeId, photoId, maxWidthPx, maxHeightPx);
    }
    @GraphQlExceptionHandler(ExternalApiException.class)
    public GraphQLError handleExternalApiException(ExternalApiException e) {
        ErrorType errorType = e.getStatusCode() / 100 == 4 ? ErrorType.BAD_REQUEST : ErrorType.INTERNAL_ERROR;
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

    public record NearByPlacesInput(
            @NotNull
            @Range(min = -90, max = 90, message = "latitude must be between -90 and 90")
            Double latitude,

            @NotNull
            @Range(min = -180, max = 180, message = "longitude must be between -180 and 180")
            Double longitude,

            @NotNull
            @Range(min = 0, max = 50000, message = "radiusMeters must be between 0 and 50000")
            Integer radiusMeters,
            @Range(min = 1, max = 20, message = "maxResultCount must be between 1 and 20")
            Integer maxResultCount,

            PlaceType[] placeTypes,
            Boolean distanceSort
    ) {
    }



}
