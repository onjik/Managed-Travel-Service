package click.porito.managed_travel.place.domain.request.command;

import click.porito.managed_travel.place.domain.OperationTime;
import click.porito.managed_travel.place.domain.PlaceCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.geojson.Point;
import org.geojson.Polygon;

import java.time.Instant;
import java.util.List;

@Getter @Setter
public class OfficialPlaceUpdateRequest extends AbstractPlaceCommandRequestBase{
    @NotNull(message = "placeId is required")
    private Long placeId;
    private String googlePlaceId;

    @Builder
    public OfficialPlaceUpdateRequest(String name, List<String> keywords, String address, String postalCode, String phoneNumber, String website, String summary, Point location, Polygon boundary, Instant createdAt, Instant updatedAt, List<PlaceCategory> categories, List<OperationTime> operationTimes, Long placeId, String googlePlaceId) {
        super(name, keywords, address, postalCode, phoneNumber, website, summary, location, boundary, createdAt, updatedAt, categories, operationTimes);
        this.placeId = placeId;
        this.googlePlaceId = googlePlaceId;
    }
}
