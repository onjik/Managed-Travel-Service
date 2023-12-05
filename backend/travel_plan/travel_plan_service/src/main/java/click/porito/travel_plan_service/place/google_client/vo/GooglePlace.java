package click.porito.travel_plan_service.place.google_client.vo;

import click.porito.travel_plan_service.place.dto.BusinessStatus;
import click.porito.travel_plan_service.place.dto.Coordinate;
import click.porito.travel_plan_service.place.dto.PlacePhotoReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlace {
    private String id;
    private LocalizedString displayName;
    private LocalizedString editorialSummary;
    private String formattedAddress;
    private Coordinate location;
    private Float rating;
    private Long userRatingCount;
    private AddressComponent[] addressComponents;
    private BusinessStatus businessStatus;
    private String[] types;
    private PlacePhotoReference[] photos;
    private Integer utcOffsetMinutes;
    private String primaryType;
    private LocalizedString primaryTypeDisplayName;
    private String iconBackgroundColor;
    private String iconMaskBaseUri;
    private String googleMapsUri;
}
