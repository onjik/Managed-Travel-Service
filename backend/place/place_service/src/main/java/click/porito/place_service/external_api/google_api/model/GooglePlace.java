package click.porito.place_service.external_api.google_api.model;

import click.porito.place_service.model.PlaceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlace implements PlaceDto {
    private String id;
    private LocalizedString displayName;
    private Coordinate location;
    private String formattedAddress;
    private LocalizedString editorialSummary;
    private String[] types;
    private GooglePhotoReference[] photos;

    @Override
    public String getName() {
        return displayName != null ? displayName.getText() : null;
    }

    @Override
    public String[] getTags() {
        return types;
    }

    @Override
    public String getAddress() {
        return formattedAddress;
    }

    @Override
    public Double getLatitude() {
        return location != null ? location.getLatitude() : null;
    }

    @Override
    public Double getLongitude() {
        return location != null ? location.getLongitude() : null;
    }

    @Override
    public String getSummary() {
        return editorialSummary != null ? editorialSummary.getText() : null;
    }

}
