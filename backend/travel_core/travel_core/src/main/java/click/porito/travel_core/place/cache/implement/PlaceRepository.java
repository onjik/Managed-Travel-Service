package click.porito.travel_core.place.cache.implement;

import click.porito.travel_core.place.cache.model.Place;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Optional;

public interface PlaceRepository extends MongoRepository<Place, String> {
    Optional<Place> findByIdAndUpdatedAtAfter(String id, Instant updatedAt);
}
