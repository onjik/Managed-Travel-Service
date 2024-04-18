package click.porito.managed_travel.place.domain.view;

import click.porito.managed_travel.place.domain.PlaceCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class OfficialPlaceView extends PlaceView {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String googlePlaceId;

    private boolean isPublic;
    private Long publisherId;

    @Builder
    public OfficialPlaceView(Long placeId, String name, List<String> keywords, String address, String postalCode, String phoneNumber, String website, String summary, Point location, Polygon boundary, Instant createdAt, Instant updatedAt, List<PlaceCategory> categories, List<OperationTimeView> operationTimeViews, String googlePlaceId, boolean isPublic, Long publisherId) {
        super(placeId, name, keywords, address, postalCode, phoneNumber, website, summary, location, boundary, createdAt, updatedAt, categories, operationTimeViews);
        this.googlePlaceId = googlePlaceId;
        this.isPublic = isPublic;
        this.publisherId = publisherId;
    }
}