package click.porito.managed_travel.place.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geojson.Point;
import org.geojson.Polygon;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class Place{
    private Long placeId;
    private String name;
    private List<String> keywords;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String website;
    private String summary;
    private Point location;
    private Polygon boundary;
    private List<PlaceCategory> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isGooglePlace;
    private List<OperationTime> operationTimes;
}
