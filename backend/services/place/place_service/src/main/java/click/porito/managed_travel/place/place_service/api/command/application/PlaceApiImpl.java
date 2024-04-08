package click.porito.managed_travel.place.place_service.api.command.application;

import click.porito.common.util.Mapper;
import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.api.command.PlaceApi;
import click.porito.managed_travel.place.domain.api.request.PlaceUpsertCommand;
import click.porito.managed_travel.place.domain.exception.PlaceApiFailedException;
import click.porito.managed_travel.place.domain.exception.PlaceNotFoundException;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.CategoryEntity;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.PlaceEntity;
import click.porito.managed_travel.place.place_service.repository.jpa.repository.CategoryEntityRepository;
import click.porito.managed_travel.place.place_service.repository.jpa.repository.PlaceRepository;
import click.porito.managed_travel.place.place_service.util.ValidatedArgs;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceApiImpl implements PlaceApi {
    private final PlaceRepository placeRepository;
    private final CategoryEntityRepository categoryEntityRepository;
    private final Mapper<PlaceEntity, Place> placeMapper;
    private final Mapper<org.geojson.Point, org.locationtech.jts.geom.Point> pointMapper;
    private final Mapper<org.geojson.Polygon, org.locationtech.jts.geom.Polygon> polygonMapper;

    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional
    @ValidatedArgs
    @Override
    public Place upsertPlace(PlaceUpsertCommand command) {
        PlaceEntity entity = null;
        if (command.isCreateCommand()){
            entity = new PlaceEntity();
        } else if (command.isUpdateCommand()) {
            Long placeId = command.getPlaceId();
            entity = placeRepository.findById(placeId)
                    .orElseThrow(() -> new PlaceNotFoundException(placeId.toString()));
        } else {
            throw new IllegalArgumentException("Invalid command");
        }
        entity.setName(command.getName());
        entity.setKeywords(command.getTags());
        entity.setAddress(command.getAddress());
        entity.setPostalCode(command.getPostalCode());
        entity.setPhoneNumber(command.getPhoneNumber());
        entity.setWebsite(command.getWebsite());
        entity.setSummary(command.getSummary());
        Point location = pointMapper.map(command.getLocation());
        entity.setLocation(location);
        if (command.getBoundary() != null) {
            Polygon boundary = polygonMapper.map(command.getBoundary());
            entity.setBoundary(boundary);
        }
        if (command.getCategories() != null) {
            List<CategoryEntity> categoryEntities = categoryEntityRepository.findByCategoryIn(command.getCategories());
            if (categoryEntities.size() != command.getCategories().size()) {
                throw new IllegalArgumentException("Invalid category");
            }
            entity.setCategories(categoryEntities);
        }
        entity.setIsPublic(command.getIsPublic());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setGooglePlaceId(command.getGooglePlaceId());
        try {
            PlaceEntity saved = placeRepository.save(entity);
            return placeMapper.map(saved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Invalid place data");
        } catch (OptimisticLockingFailureException e) {
            throw new PlaceApiFailedException(e);
        } catch (Exception e) {
            throw new PlaceApiFailedException(e);
        }
    }

    @Transactional
    @Override
    public boolean deletePlace(Long placeId) {
        if (placeRepository.existsById(placeId)) {
            placeRepository.deleteById(placeId);
            return true;
        }
        return false;
    }

}
