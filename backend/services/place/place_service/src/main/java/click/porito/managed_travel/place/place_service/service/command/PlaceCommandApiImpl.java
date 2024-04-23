package click.porito.managed_travel.place.place_service.service.command;

import click.porito.common.exception.Domain;
import click.porito.common.exception.common.AccessDeniedException;
import click.porito.managed_travel.place.domain.api.command.PlaceCommandApi;
import click.porito.managed_travel.place.domain.exception.PlaceResourceNotFoundException;
import click.porito.managed_travel.place.domain.request.command.DayOperationTimePutRequest;
import click.porito.managed_travel.place.domain.request.command.OperationTimePutRequest;
import click.porito.managed_travel.place.domain.request.command.PlaceUpsertRequest;
import click.porito.managed_travel.place.domain.view.PlaceView;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.DayOperationTimeEntity;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.OperationTimeEntity;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.PlaceEntity;
import click.porito.managed_travel.place.place_service.repository.jpa.repository.PlaceCategoryRepository;
import click.porito.managed_travel.place.place_service.repository.jpa.repository.PlaceRepository;
import click.porito.managed_travel.place.place_service.util.AccountContextStrategy;
import click.porito.managed_travel.place.place_service.util.ValidatedArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
@RequiredArgsConstructor
public class PlaceCommandApiImpl implements PlaceCommandApi {
    private final AccountContextStrategy accountContextStrategy;

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceRepository placeRepository;


    @Override
    @ValidatedArgs(nullable = false)
    public PlaceView upsertPlace(PlaceUpsertRequest request) {
        var accountEntity = accountContextStrategy.getAccountSnapshot()
                .orElseThrow(() -> new AccessDeniedException(Domain.PLACE));
        final PlaceEntity placeEntity;
        if (request.getPlaceId() == null) {
            placeEntity = new PlaceEntity();
            placeEntity.setPublisher(accountEntity);
        } else {
            placeEntity = placeRepository.findByPlaceIdForUpdate(request.getPlaceId())
                    .orElseThrow(PlaceResourceNotFoundException::new);
        }
        overwritePlaceEntity(placeEntity, request);
        PlaceEntity saved = placeRepository.save(placeEntity);
        return PlaceEntity.toView(saved);
    }

    protected void overwritePlaceEntity(PlaceEntity placeEntity, PlaceUpsertRequest request) {
        placeEntity.setName(request.getName());
        placeEntity.setKeywords(request.getKeywords());
        placeEntity.setAddress(request.getAddress());
        placeEntity.setPostalCode(request.getPostalCode());
        placeEntity.setPhoneNumber(request.getPhoneNumber());
        placeEntity.setWebsite(request.getWebsite());
        placeEntity.setSummary(request.getSummary());
        placeEntity.setLocationByGeoJson(request.getLocation());
        placeEntity.setBoundaryByGeoJson(request.getBoundary());
        var categoryEntities = placeCategoryRepository.findByCategoryIn(request.getCategories());
        if (categoryEntities.size() != request.getCategories().size()) {
            throw new IllegalArgumentException("invalid categories");
        }
        placeEntity.setCategories(Set.copyOf(categoryEntities));
        placeEntity.setGooglePlaceId(request.getGooglePlaceId());
        placeEntity.setIsPublic(getOrDefault(request.getIsPublic(), false));
        placeEntity.setIsOfficial(getOrDefault(request.getIsOfficial(), false));
        upsertOperationTimes(placeEntity, request.getOperationTimes());
    }

    protected void upsertOperationTimes(PlaceEntity placeEntity, List<OperationTimePutRequest> operationTimePutRequests) {
        // merge 전략을 사용한다.
        List<OperationTimeEntity> operationTimeEntities = new ArrayList<>();
        for (OperationTimePutRequest operationTimePutRequest : operationTimePutRequests) {
            var timeEntity = new OperationTimeEntity();
            timeEntity.setPlaceEntity(placeEntity);
            timeEntity.setStartDate(operationTimePutRequest.getStartDate());
            timeEntity.setEndDate(operationTimePutRequest.getEndDate());
            timeEntity.setDayOperationTimeEntities(new ArrayList<>());

            for (DayOperationTimePutRequest dayOperationTimePutRequest : operationTimePutRequest.getDayOperationTimes()) {
                var dayTimeEntity = new DayOperationTimeEntity();
                dayTimeEntity.setOperationTimeEntity(timeEntity);
                dayTimeEntity.setDayOperationTimeId(dayOperationTimePutRequest.getDayOperationTimeId());
                dayTimeEntity.setStartTime(dayOperationTimePutRequest.getStartTime());
                dayTimeEntity.setEndTime(dayOperationTimePutRequest.getEndTime());
                dayTimeEntity.setNextDayLinked(getOrDefault(dayOperationTimePutRequest.getNextDayLinked(), false));
                timeEntity.getDayOperationTimeEntities().add(dayTimeEntity);
            }
            operationTimeEntities.add(timeEntity);
        }
        placeEntity.setOperationTimes(operationTimeEntities);
    }

    @Override
    public void deletePlace(Long placeId) {
        Assert.notNull(placeId, "placeId must not be null");
        var placeEntity = placeRepository.findByPlaceIdForUpdate(placeId)
                .orElseThrow(PlaceResourceNotFoundException::new);
        placeRepository.delete(placeEntity);
    }

    protected <T> T getOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
