package click.porito.managed_travel.place.place_service.operation.adapter.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;


@Getter @Setter
@Builder
@TypeAlias("place")
@Document(collection = "place")
public class PlaceEntity {
    @Id
    private String id;
    private String name;
    private List<String> tags;
    private String address;
    private GeoJsonPoint location;
    private String summary;
    private List<PhotoEntity> photos;
    @LastModifiedDate
    private Instant updatedAt;

}
