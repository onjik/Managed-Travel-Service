package click.porito.place_service.cache;

import click.porito.place_service.cache.model.PlaceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Optional;

public interface PlaceRepository extends MongoRepository<PlaceDocument, String> {
    Optional<PlaceDocument> findByIdAndUpdatedAtAfter(String id, Instant updatedAt);
}
