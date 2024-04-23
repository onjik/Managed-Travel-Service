package click.porito.managed_travel.place.domain.request.command;

import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.domain.util.SingleChunkPolygonConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PlaceUpsertRequest implements UpsertRequest{
    private Long placeId;
    @NotBlank(message = "name is required")
    private String name;
    private List<String> keywords = new ArrayList<>();
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String website;
    private String summary;
    @NotNull(message = "location is required")
    private org.geojson.Point location;
    @SingleChunkPolygonConstraint
    private org.geojson.Polygon boundary;
    @NotEmpty(message = "categories is required")
    private List<PlaceCategory> categories = new ArrayList<>();
    private List<OperationTimePutRequest> operationTimes = new ArrayList<>();
    private String googlePlaceId;
    private Boolean isPublic;
    private Boolean isOfficial;

    @Override
    public boolean isCreateRequest() {
        return placeId == null;
    }

    @Override
    public boolean isUpdateRequest() {
        return placeId != null;
    }
}
