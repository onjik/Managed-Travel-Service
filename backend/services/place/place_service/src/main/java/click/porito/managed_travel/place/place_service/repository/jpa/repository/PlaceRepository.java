package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.place_service.repository.jpa.entity.PlaceEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends org.springframework.data.repository.Repository<PlaceEntity, Long> {
    Optional<PlaceEntity> findByPlaceId(Long placeId);

    PlaceEntity save(PlaceEntity entity);

}
