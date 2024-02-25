package click.porito.managed_travel.place.place_service.operation.adapter.google_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlace {
    private String id;
    private LocalizedString displayName;
    private Coordinate location;
    private String formattedAddress;
    private LocalizedString editorialSummary;
    private List<String> types;
    private List<GooglePhotoReference> photos;

}
