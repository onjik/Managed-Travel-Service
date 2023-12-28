package click.porito.place_service.cache.model;

import click.porito.place_service.model.PlaceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Arrays;


@Getter @Setter
@Builder
@TypeAlias("place")
@Document(collection = "place")
public class PlaceDocument implements PlaceDto {
    @Id
    private String id;
    private String name;
    private String[] tags;
    private String address;
    private GeoJsonPoint location;
    private String summary;
    private PhotoReferenceDocument[] photos;
    @LastModifiedDate
    private Instant updatedAt;

    @Override
    public Double getLatitude() {
        return location.getY();
    }

    @Override
    public Double getLongitude() {
        return location.getX();
    }

    public static PlaceDocument from(PlaceDto placeDto) {
        Assert.notNull(placeDto, "placeDto must not be null");
        Assert.notNull(placeDto.getId(), "placeDto.id must not be null");
        Assert.notNull(placeDto.getName(), "placeDto.name must not be null");
        Assert.notNull(placeDto.getTags(), "placeDto.tags must not be null");
        Assert.notNull(placeDto.getLatitude(), "placeDto.latitude must not be null");
        Assert.notNull(placeDto.getLongitude(), "placeDto.longitude must not be null");

        Double latitude = placeDto.getLatitude();
        Double longitude = placeDto.getLongitude();
        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);

        var photos = Arrays.stream(placeDto.getPhotos())
                .map(PhotoReferenceDocument::from)
                .toArray(PhotoReferenceDocument[]::new);

        return PlaceDocument.builder()
                .id(placeDto.getId())
                .name(placeDto.getName())
                .tags(placeDto.getTags())
                .address(placeDto.getAddress())
                .location(location)
                .summary(placeDto.getSummary())
                .photos(photos)
                .build();
    }
}
