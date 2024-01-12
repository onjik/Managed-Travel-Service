package click.porito.travel_core.place.cache.implement;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.cache.PlaceCacheService;
import click.porito.travel_core.place.cache.model.Place;
import click.porito.travel_core.place.dto.PlaceView;
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

    private final Mapper<PlaceView, Place> placeViewMapper;
    private final Mapper<Place,PlaceView> placeMapper;
    private Duration cacheDuration;

    @Override
    public Optional<PlaceView> get(String placeId) {
        Instant updatedAt = Instant.now().minus(cacheDuration);
        return placeRepository.findByIdAndUpdatedAtAfter(placeId, updatedAt)
                .map(placeMapper::map);
    }

    @Override
    public void put(PlaceView placeView) {
        Place document = placeViewMapper.map(placeView);
        placeRepository.save(document);
    }

    @Override
    public void putAll(List<PlaceView> placeViews) {
        var documents = placeViewMapper.map(placeViews);
        placeRepository.saveAll(documents);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long seconds = placeCacheConfiguration.getDurationSeconds();
        cacheDuration = Duration.ofSeconds(seconds);
    }
}
