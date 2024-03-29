package click.porito.managed_travel.place.place_service.api.adapter.rest;


import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.api.PlaceApi;
import click.porito.managed_travel.place.domain.api.request.NearBySearchQuery;
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

    @GetMapping
    public ResponseEntity<List<Place>> getPlaces(@RequestParam String[] placeIds) {
        return ResponseEntity.ok(placeApi.getPlaces(placeIds));
    }

    @PostMapping("/searchNearBy")
    public List<Place> searchNearBy(@RequestBody NearBySearchQuery body) {
        return placeApi.getNearbyPlaces(body);
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


}
