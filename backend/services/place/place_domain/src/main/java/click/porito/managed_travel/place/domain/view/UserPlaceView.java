package click.porito.managed_travel.place.domain.view;

import click.porito.managed_travel.place.domain.PlaceCategory;
import lombok.Builder;
import org.geojson.Point;
import org.geojson.Polygon;

import java.time.Instant;
import java.util.List;

public class UserPlaceView extends PlaceView {

    @Builder
    public UserPlaceView(Long placeId, String name, List<String> keywords, String address, String postalCode, String phoneNumber, String website, String summary, Point location, Polygon boundary, Instant createdAt, Instant updatedAt, List<PlaceCategory> categories, List<OperationTimeView> operationTimeViews) {
        super(placeId, name, keywords, address, postalCode, phoneNumber, website, summary, location, boundary, createdAt, updatedAt, categories, operationTimeViews);
    }

    public UserPlaceView() {
    }
}
