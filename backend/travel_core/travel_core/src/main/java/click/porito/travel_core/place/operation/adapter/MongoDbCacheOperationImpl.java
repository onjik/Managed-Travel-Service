package click.porito.travel_core.place.operation.adapter;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.operation.adapter.config.PlaceCacheConfiguration;
import click.porito.travel_core.place.operation.application.PlaceCacheOperation;
import click.porito.travel_core.place.domain.Place;
import click.porito.travel_core.place.operation.adapter.persistence.entity.PlaceEntity;
import click.porito.travel_core.place.operation.adapter.persistence.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MongoDbCacheOperationImpl implements PlaceCacheOperation, InitializingBean {

    private final PlaceCacheConfiguration placeCacheConfiguration;

    private final PlaceRepository placeRepository;

    private final Mapper<Place, PlaceEntity> placeViewMapper;
    private final Mapper<PlaceEntity, Place> placeMapper;
    private Duration cacheDuration;

    @Override
    public Optional<Place> get(String placeId) {
        Instant updatedAt = Instant.now().minus(cacheDuration);
        return placeRepository.findByIdAndUpdatedAtAfter(placeId, updatedAt)
                .map(placeMapper::map);
    }

    @Override
    public List<Place> getByIdIn(String[] placeIds) {
        Instant updatedAt = Instant.now().minus(cacheDuration);
        return placeRepository.findByIdInAndUpdatedAtAfter(List.of(placeIds), updatedAt)
                .stream().map(placeMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void put(Place place) {
        PlaceEntity document = placeViewMapper.map(place);
        placeRepository.save(document);
    }

    @Override
    public void putAll(List<Place> places) {
        var documents = placeViewMapper.map(places);
        placeRepository.saveAll(documents);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long seconds = placeCacheConfiguration.getDurationSeconds();
        cacheDuration = Duration.ofSeconds(seconds);
    }
}
