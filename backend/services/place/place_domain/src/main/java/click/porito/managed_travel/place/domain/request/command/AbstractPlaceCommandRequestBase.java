package click.porito.managed_travel.place.domain.request.command;

import click.porito.managed_travel.place.domain.OperationTime;
import click.porito.managed_travel.place.domain.PlaceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public abstract class AbstractPlaceCommandRequestBase {
    @NotBlank(message = "name is required")
    private String name;
    private List<String> keywords;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String website;
    private String summary;
    @NotNull(message = "location is required")
    private org.geojson.Point location;
    private org.geojson.Polygon boundary;
    private Instant createdAt;
    private Instant updatedAt;
    @NotBlank(message = "categories is required")
    private List<PlaceCategory> categories;
    private List<OperationTime> operationTimes;
}
