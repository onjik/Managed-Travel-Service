package click.porito.travel_core.place.adapter.api.rest;


import click.porito.travel_core.place.application.api.PlaceApi;
import click.porito.travel_core.place.PlaceType;
import click.porito.travel_core.place.domain.Place;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/places")
@RequiredArgsConstructor
public class PlaceRestApi {

    private final PlaceApi placeApi;

    @GetMapping("/{placeId}")
    public ResponseEntity<Place> getPlace(@PathVariable String placeId) {
        Optional<Place> place = placeApi.getPlace(placeId);
        return place.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/searchNearBy")
    public List<Place> searchNearBy(@Valid @RequestBody NearBySearchRequestBody body) {
        return placeApi.getNearbyPlaces(
                body.latitude(),
                body.longitude(),
                body.radiusMeters(),
                body.maxResultCount(),
                body.placeTypes(),
                body.distanceSort()
        );
    }

    @GetMapping("/{placeId}/photos/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable String placeId, @PathVariable String photoId,
                                   @RequestParam(name = "maxHeightPx",required = false, defaultValue = "1080") @Range(min = 1, max = 4800, message = "1 < maxHeightPx < 4800")
                                           Integer maxHeightPx,
                                   @RequestParam(name = "maxWidthPx",required = false, defaultValue = "1920") @Range(min = 1, max = 4800, message = "1 < maxWidthPx < 4800")
                                          Integer maxWidthPx
    ) {
        Optional<String> photoUrl = placeApi.getPhotoUrl(placeId, photoId, maxWidthPx, maxHeightPx);

        if (photoUrl.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String url = photoUrl.get();
        //redirect
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(url));
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
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
