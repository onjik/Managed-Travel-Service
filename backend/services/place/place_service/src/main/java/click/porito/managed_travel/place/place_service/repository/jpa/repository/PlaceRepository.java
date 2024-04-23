package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.place_service.repository.jpa.entity.PlaceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends org.springframework.data.repository.Repository<PlaceEntity, Long> {
    Optional<PlaceEntity> findByPlaceId(Long placeId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PlaceEntity p WHERE p.placeId = :placeId")
    Optional<PlaceEntity> findByPlaceIdForUpdate(Long placeId);

    PlaceEntity save(PlaceEntity entity);
    void delete(PlaceEntity entity);
}
