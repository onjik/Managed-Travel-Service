package click.porito.travel_core.place.adapter.operation.persistence.repository;

import click.porito.travel_core.place.adapter.operation.persistence.entity.PlaceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends MongoRepository<PlaceEntity, String> {
    Optional<PlaceEntity> findByIdAndUpdatedAtAfter(String id, Instant updatedAt);

    List<PlaceEntity> findByIdInAndUpdatedAtAfter(List<String> ids, Instant updatedAt);
}
