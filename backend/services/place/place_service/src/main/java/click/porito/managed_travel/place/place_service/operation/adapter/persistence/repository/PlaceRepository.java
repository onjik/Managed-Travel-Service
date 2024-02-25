package click.porito.managed_travel.place.place_service.operation.adapter.persistence.repository;

import click.porito.managed_travel.place.place_service.operation.adapter.persistence.entity.PlaceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends MongoRepository<PlaceEntity, String> {
    Optional<PlaceEntity> findByIdAndUpdatedAtAfter(String id, Instant updatedAt);

    List<PlaceEntity> findByIdInAndUpdatedAtAfter(List<String> ids, Instant updatedAt);

    boolean existsByIdAndUpdatedAtAfter(String placeId, Instant updatedAt);
}
