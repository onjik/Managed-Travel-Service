package click.porito.managed_travel.place.domain.request.command;

import click.porito.managed_travel.place.domain.PlaceCategory;
import jakarta.validation.constraints.NotNull;
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
public class UserPlaceUpdateRequest extends AbstractPlaceCommandRequestBase{
    @NotNull(message = "placeId is required")
    private Long placeId;

    @Builder
    public UserPlaceUpdateRequest(String name, List<String> keywords, String address, String postalCode, String phoneNumber, String website, String summary, Point location, Polygon boundary, Instant createdAt, Instant updatedAt, List<PlaceCategory> categories, Long placeId) {
        super(name, keywords, address, postalCode, phoneNumber, website, summary, location, boundary, createdAt, updatedAt, categories);
        this.placeId = placeId;
    }
}
