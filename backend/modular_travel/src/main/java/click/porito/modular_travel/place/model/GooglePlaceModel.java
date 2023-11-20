package click.porito.modular_travel.place.model;

import click.porito.modular_travel.image.Photo;
import click.porito.modular_travel.place.Coordinate;
import click.porito.modular_travel.place.LocalizedString;
import click.porito.modular_travel.place.GooglePlace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlaceModel implements GooglePlace {
    private String id;
    private BusinessStatus businessStatus;
    private LocalizedString displayName;
    private Coordinate location;
    private String formattedAddress;
    private String shortFormattedAddress;
    private String[] types;
    private Integer utcOffsetMinutes;
    private Photo[] photos;
    private String primaryType;
    private LocalizedString primaryTypeDisplayName;
    private String iconBackgroundColor;
    private String iconMaskBaseUri;
    private String googleMapsUri;
}
