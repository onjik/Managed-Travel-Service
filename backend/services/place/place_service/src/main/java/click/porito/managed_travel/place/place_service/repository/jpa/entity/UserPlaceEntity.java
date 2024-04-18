package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.domain.view.OperationTimeView;
import click.porito.managed_travel.place.domain.view.UserPlaceView;
import click.porito.managed_travel.place.place_service.util.GeoUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "user_place")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserPlaceEntity extends PlaceEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountSnapshotEntity accountSnapshotEntity;

    public static UserPlaceView toView(UserPlaceEntity userPlaceEntity) {
        List<PlaceCategory> placeCategories = userPlaceEntity.getCategories()
                .stream()
                .map(CategoryEntity::getCategory)
                .toList();

        List<OperationTimeView> operationTimeViews = userPlaceEntity.getOperationTimes()
                .stream()
                .map(OperationTimeEntity::toView)
                .toList();

        return UserPlaceView.builder()
                .placeId(userPlaceEntity.getPlaceId())
                .name(userPlaceEntity.getName())
                .keywords(userPlaceEntity.getKeywords())
                .address(userPlaceEntity.getAddress())
                .postalCode(userPlaceEntity.getPostalCode())
                .phoneNumber(userPlaceEntity.getPhoneNumber())
                .website(userPlaceEntity.getWebsite())
                .summary(userPlaceEntity.getSummary())
                .location(GeoUtils.jtsPointToGeoJsonPointMapper().map(userPlaceEntity.getLocation()))
                .boundary(GeoUtils.jtsPolygonToGeoJsonPolygonMapper().map(userPlaceEntity.getBoundary()))
                .createdAt(userPlaceEntity.getCreatedAt())
                .updatedAt(userPlaceEntity.getUpdatedAt())
                .categories(placeCategories)
                .operationTimeViews(operationTimeViews)
                .build();
    }
}
