package click.porito.managed_travel.place.domain.view;

import click.porito.managed_travel.place.domain.PlaceCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geojson.Point;
import org.geojson.Polygon;

import java.time.Instant;
import java.util.List;

import static click.porito.managed_travel.place.domain.PlaceDomainConstant.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PlaceView {
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant updatedAt;
    private List<PlaceCategory> categories;
    private List<OperationTimeView> operationTimeViews;
}
