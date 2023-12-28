package click.porito.place_service.presentation;


import click.porito.place_service.PhotoService;
import click.porito.place_service.PlaceService;
import click.porito.place_service.PlaceType;
import click.porito.place_service.external_api.ExternalApiException;
import click.porito.place_service.model.PlaceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/places")
@RequiredArgsConstructor
public class PlaceRestApi {

    private final PlaceService placeService;
    private final PhotoService photoService;

    @GetMapping("/{placeId}")
    public PlaceDto getPlace(@PathVariable String placeId) {
        return placeService.getPlace(placeId);
    }

    @PostMapping("/searchNearBy")
    public List<PlaceDto> searchNearBy(@Valid @RequestBody NearBySearchRequestBody body) {
        return placeService.getNearbyPlaces(
                body.latitude(),
                body.longitude(),
                body.radiusMeters(),
                body.maxResultCount(),
                body.placeTypes(),
                body.distanceSort()
        );
    }
    //MethodArgumentNotValidException

    @GetMapping("/{placeId}/photos/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable String placeId, @PathVariable String photoId,
                                   @RequestParam(name = "maxHeightPx",required = false, defaultValue = "1080") @Range(min = 1, max = 4800, message = "1 < maxHeightPx < 4800")
                                           Integer maxHeightPx,
                                   @RequestParam(name = "maxWidthPx",required = false, defaultValue = "1920") @Range(min = 1, max = 4800, message = "1 < maxWidthPx < 4800")
                                          Integer maxWidthPx
    ) {
        String url = photoService.getPhotoUrl(placeId, photoId, maxWidthPx, maxHeightPx);
        //redirect
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(url));
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApiException(ExternalApiException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors().stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage()
                ))
                .toList();
        return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("errors", errors));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        var errors = e.getValueResults().stream()
                .map(result -> Map.of(
                        "field", result.getMethodParameter().getParameterName(),
                        "message", result.getResolvableErrors().stream()
                                .map(MessageSourceResolvable::getDefaultMessage)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(", "))
                ))
                .toList();
        return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("errors", errors));
    }

    public record NearBySearchRequestBody(
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
