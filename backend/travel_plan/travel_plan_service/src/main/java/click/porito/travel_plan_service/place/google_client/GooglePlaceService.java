package click.porito.travel_plan_service.place.google_client;

import click.porito.travel_plan_service.place.PlaceService;
import click.porito.travel_plan_service.place.dto.PlaceView;
import click.porito.travel_plan_service.place.google_client.vo.AddressComponent;
import click.porito.travel_plan_service.place.google_client.vo.GooglePlace;
import click.porito.travel_plan_service.place.google_client.vo.LocalizedString;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class GooglePlaceService implements PlaceService {
    private final static String GOOGLE_API_HEADER = "X-Goog-Api-Key";
    private final static String PLACE_FIELD_MASK = "X-Goog-FieldMask";
    private final static String LANGUAGE_CODE = "languageCode";
    private final RestClient restClient;

    public GooglePlaceService(RestClient.Builder restClientBuilder, GoogleApiContext context) {

        this.restClient = restClientBuilder.baseUrl("https://places.googleapis.com/v1")
                .defaultHeader(GOOGLE_API_HEADER, context.apiKey())
                .build();
    }

    static final String DETAILED_INFO;
    static {
        var detailedInfo = new FieldMask[]{
                FieldMask.id,
                FieldMask.displayName,
                FieldMask.editorialSummary,
                FieldMask.formattedAddress,
                FieldMask.location,
                FieldMask.rating,
                FieldMask.userRatingCount,
                FieldMask.addressComponents,
                FieldMask.businessStatus,
                FieldMask.types,
                FieldMask.photos
        };

        var sb = new StringBuilder();
        for (var field : detailedInfo) {
            sb.append(field.getMaskName()).append(",");
        }
        DETAILED_INFO = sb.toString();
    }

    @Override
    public PlaceView getPlace(String placeId) {
        Locale locale = LocaleContextHolder.getLocale();
        ResponseEntity<GooglePlace> response = restClient.get()
                .uri("/places/{placeId}", placeId)
                .header(PLACE_FIELD_MASK, DETAILED_INFO)
                .header(LANGUAGE_CODE, locale.getLanguage())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        ((req, res) -> {
                            String body = res.getBody().toString();
                            HttpStatusCode statusCode = res.getStatusCode();
                            throw  new GoogleApiCallFailedException(statusCode, body);
                        }))
                .toEntity(GooglePlace.class);
        HttpStatusCode statusCode = response.getStatusCode();
        if (response.getBody() == null) {
            throw new GoogleApiCallFailedException(statusCode, null);
        }

        GooglePlace placeDetailResponse = response.getBody();
        return convert(placeDetailResponse);
    }

    @Override
    public PlaceView getPlaceNearby(double latitude, double longitude, int radius) {
        return null;
    }


    private PlaceView convert(GooglePlace place) {
        Assert.notNull(place, "place must not be null");
        Assert.notNull(place.getId(), "place.id must not be null");
        Locale locale = LocaleContextHolder.getLocale();
        String id = place.getId();
        var name = Optional.ofNullable(place.getDisplayName())
                .map(LocalizedString::toMap)
                .orElse(null);
        var address = Optional.ofNullable(place.getFormattedAddress())
                .map(s -> Map.of(locale, s))
                .orElse(null);
        var coordinate = place.getLocation();
        var rating = place.getRating();
        var userRatingCount = place.getUserRatingCount();
        var businessStatus = place.getBusinessStatus();
        var types = place.getTypes();
        var photos = place.getPhotos();
        AddressComponent[] addressComponents = place.getAddressComponents();
        Map<Locale, String> country = null;
        Map<Locale, String> city = null;
        if (addressComponents != null){
            for (AddressComponent addressComponent : addressComponents) {
                if (Arrays.asList(addressComponent.types()).contains("country")){
                    String languageCode = addressComponent.languageCode();
                    Locale locale1 = new Locale(languageCode);
                    country = Map.of(locale1, addressComponent.longText());
                } else if (Arrays.asList(addressComponent.types()).contains("locality")){
                    String languageCode = addressComponent.languageCode();
                    Locale locale1 = new Locale(languageCode);
                    city = Map.of(locale1, addressComponent.longText());
                }
            }
        }
        return PlaceView.builder()
                .id(id)
                .name(name)
                .address(address)
                .coordinate(coordinate)
                .rating(rating)
                .userRatingCount(userRatingCount)
                .country(country)
                .city(city)
                .businessStatus(businessStatus)
                .types(types)
                .photos(photos)
                .build();
    }
}
