package click.porito.place_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlace implements Place {
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
