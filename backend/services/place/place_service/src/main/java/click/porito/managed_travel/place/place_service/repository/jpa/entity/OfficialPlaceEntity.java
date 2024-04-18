package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.domain.view.OfficialPlaceView;
import click.porito.managed_travel.place.domain.view.OperationTimeView;
import click.porito.managed_travel.place.place_service.util.GeoUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "official_place")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class OfficialPlaceEntity extends PlaceEntity {
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "google_place_id", unique = true)
    private String googlePlaceId;

    @OneToMany(mappedBy = "placeEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<ReviewEntity> reviews;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "publisher_id")
    private AccountSnapshotEntity publisher;

    public static OfficialPlaceView toView(OfficialPlaceEntity officialPlaceEntity) {
        List<PlaceCategory> placeCategories = officialPlaceEntity.getCategories()
                .stream()
                .map(CategoryEntity::getCategory)
                .toList();
        List<OperationTimeView> operationTimeViews = officialPlaceEntity.getOperationTimes()
                .stream()
                .map(OperationTimeEntity::toView)
                .toList();
        return OfficialPlaceView.builder()
                .placeId(officialPlaceEntity.getPlaceId())
                .name(officialPlaceEntity.getName())
                .keywords(officialPlaceEntity.getKeywords())
                .address(officialPlaceEntity.getAddress())
                .postalCode(officialPlaceEntity.getPostalCode())
                .phoneNumber(officialPlaceEntity.getPhoneNumber())
                .website(officialPlaceEntity.getWebsite())
                .summary(officialPlaceEntity.getSummary())
                .location(GeoUtils.jtsPointToGeoJsonPointMapper().map(officialPlaceEntity.getLocation()))
                .boundary(GeoUtils.jtsPolygonToGeoJsonPolygonMapper().map(officialPlaceEntity.getBoundary()))
                .createdAt(officialPlaceEntity.getCreatedAt())
                .updatedAt(officialPlaceEntity.getUpdatedAt())
                .categories(placeCategories)
                .operationTimeViews(operationTimeViews)
                .googlePlaceId(officialPlaceEntity.getGooglePlaceId())
                .isPublic(officialPlaceEntity.getIsPublic())
                .publisherId(officialPlaceEntity.getPublisher().getAccountId())
                .build();
    }

}
