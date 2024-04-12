package click.porito.managed_travel.place.domain.request.command;

import click.porito.managed_travel.place.domain.OperationTime;
import click.porito.managed_travel.place.domain.PlaceCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geojson.Point;
import org.geojson.Polygon;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class OfficialPlaceCreateRequest extends AbstractPlaceCommandRequestBase{
    private String googlePlaceId;

    @Builder
    public OfficialPlaceCreateRequest(String name, List<String> keywords, String address, String postalCode, String phoneNumber, String website, String summary, Point location, Polygon boundary, Instant createdAt, Instant updatedAt, List<PlaceCategory> categories, List<OperationTime> operationTimes, String googlePlaceId) {
        super(name, keywords, address, postalCode, phoneNumber, website, summary, location, boundary, createdAt, updatedAt, categories, operationTimes);
        this.googlePlaceId = googlePlaceId;
    }

}
