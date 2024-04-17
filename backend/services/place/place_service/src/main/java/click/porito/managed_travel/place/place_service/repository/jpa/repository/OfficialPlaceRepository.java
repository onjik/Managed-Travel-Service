package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.place_service.repository.jpa.entity.OfficialPlaceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficialPlaceRepository extends org.springframework.data.repository.Repository<click.porito.managed_travel.place.place_service.repository.jpa.entity.OfficialPlaceEntity, Long>{
    OfficialPlaceEntity save(OfficialPlaceEntity entity);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT place FROM OfficialPlaceEntity place WHERE place.placeId = :placeId")
    Optional<OfficialPlaceEntity> findByPlaceIdForUpdate(Long placeId);
}
