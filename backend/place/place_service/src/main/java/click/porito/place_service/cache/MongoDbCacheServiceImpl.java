package click.porito.place_service.cache;

import click.porito.place_service.cache.model.PlaceDocument;
import click.porito.place_service.model.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MongoDbCacheServiceImpl implements PlaceCacheService, InitializingBean {

    private final PlaceCacheConfiguration placeCacheConfiguration;

    private final PlaceRepository placeRepository;
    private Duration cacheDuration;

    @Override
    public Optional<PlaceDto> get(String placeId) {
        Instant updatedAt = Instant.now().minus(cacheDuration);
        return placeRepository.findByIdAndUpdatedAtAfter(placeId, updatedAt)
                .map(o -> (PlaceDto) o);
    }

    @Override
    public void put(PlaceDto placeDto) {
        PlaceDocument document = PlaceDocument.from(placeDto);
        placeRepository.save(document);
    }

    @Override
    public void putAll(List<PlaceDto> placeDtos) {
        List<PlaceDocument> documents = placeDtos.stream()
                .map(PlaceDocument::from)
                .toList();
        placeRepository.saveAll(documents);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long seconds = placeCacheConfiguration.getDurationSeconds();
        cacheDuration = Duration.ofSeconds(seconds);
    }
}
