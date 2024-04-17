package click.porito.managed_travel.place.place_service.repository.jpa.repository;

import click.porito.managed_travel.place.place_service.repository.jpa.entity.UserPlaceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPlaceRepository extends org.springframework.data.repository.Repository<UserPlaceEntity, Long> {
    UserPlaceEntity save(UserPlaceEntity entity);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT place FROM UserPlaceEntity place WHERE place.placeId = :placeId AND place.accountSnapshotEntity.accountId = :accountId")
    Optional<UserPlaceEntity> findByPlaceIdAndAccountIdForUpdate(Long placeId, Long accountId);

    void delete(UserPlaceEntity entity);
}
