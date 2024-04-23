package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import click.porito.managed_travel.place.domain.view.PlaceView;
import click.porito.managed_travel.place.place_service.util.GeoUtils;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;

@Table(name = "place")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "name")
    private String name;

    @Column(name = "keywords")
    @Type(StringArrayType.class)
    private List<String> keywords = new ArrayList<>();

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website")
    private String website;
    @Column(name = "summary")
    private String summary;
    @Column(name = "location")
    private Point location;
    @Column(name = "boundary")
    private Polygon boundary;
    @Column(name = "google_place_id")
    private String googlePlaceId;
    @Column(name = "is_public")
    private Boolean isPublic;
    @Column(name = "is_official")
    private Boolean isOfficial;
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "place_category",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<PlaceArticleEntity> placeArticles;



    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<OperationTimeEntity> operationTimes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountSnapshotEntity publisher;

    public void setLocationByGeoJson(org.geojson.Point location) {
        Assert.notNull(location, "location must not be null");
        this.location = GeoUtils.geoJsonPointToJtsPointMapper().map(location);
    }


    public void setBoundaryByGeoJson(org.geojson.Polygon boundary) {
        Assert.notNull(boundary, "boundary must not be null");
        this.boundary = GeoUtils.geoJsonPolygonToJtsPolygonMapper().map(boundary);
    }

    public org.geojson.Point getLocationByGeoJson() {
        return GeoUtils.jtsPointToGeoJsonPointMapper().map(location);
    }

    public org.geojson.Polygon getBoundaryByGeoJson() {
        return GeoUtils.jtsPolygonToGeoJsonPolygonMapper().map(boundary);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.getPlaceId() == null) return false;
        PlaceEntity that = (PlaceEntity) o;
        if (that.getPlaceId() == null) return false;
        return Objects.equals(getPlaceId(), that.getPlaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlaceId());
    }

    public static PlaceView toView(PlaceEntity placeEntity) {
        return PlaceView.builder()
                .placeId(placeEntity.getPlaceId())
                .name(placeEntity.getName())
                .keywords(placeEntity.getKeywords())
                .address(placeEntity.getAddress())
                .postalCode(placeEntity.getPostalCode())
                .phoneNumber(placeEntity.getPhoneNumber())
                .website(placeEntity.getWebsite())
                .summary(placeEntity.getSummary())
                .location(placeEntity.getLocationByGeoJson())
                .boundary(placeEntity.getBoundaryByGeoJson())
                .publisherId(placeEntity.getPublisher().getAccountId())
                .createdAt(placeEntity.getCreatedAt())
                .updatedAt(placeEntity.getUpdatedAt())
                .categories(placeEntity.getCategories().stream().map(CategoryEntity::getCategory).toList())
                .operationTimeViews(placeEntity.getOperationTimes().stream().map(OperationTimeEntity::toView).toList())
                .googlePlaceId(placeEntity.getGooglePlaceId())
                .isPublic(placeEntity.getIsPublic())
                .isOfficial(placeEntity.getIsOfficial())
                .build();
    }
}
