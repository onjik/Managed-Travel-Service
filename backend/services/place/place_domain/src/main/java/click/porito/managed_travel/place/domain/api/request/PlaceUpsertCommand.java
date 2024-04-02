package click.porito.managed_travel.place.domain.api.request;

import click.porito.managed_travel.place.domain.PlaceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.geojson.Point;
import org.geojson.Polygon;

import java.util.List;

@Data
public class PlaceUpsertCommand implements UpsertCommand{
    private Long placeId; // nullable
    @NotBlank
    private String name;
    private List<String> tags;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String website;
    private String summary;
    @NotNull
    private Point location;
    private Polygon boundary;
    @NotEmpty
    private List<PlaceCategory> categories;
    private Boolean isPublic = Boolean.TRUE;
    private String googlePlaceId;

    @Override
    public boolean isUpdateCommand() {
        return placeId != null;
    }

    @Override
    public boolean isCreateCommand() {
        return placeId == null;
    }
}
