package click.porito.managed_travel.place.place_service.service.command;

import click.porito.managed_travel.place.domain.api.command.PlaceCommandApi;
import click.porito.managed_travel.place.domain.request.command.DayOperationTimePutRequest;
import click.porito.managed_travel.place.domain.request.command.OfficialPlacePutRequest;
import click.porito.managed_travel.place.domain.request.command.OperationTimePutRequest;
import click.porito.managed_travel.place.domain.request.command.UserPlacePutRequest;
import click.porito.managed_travel.place.domain.view.OfficialPlaceView;
import click.porito.managed_travel.place.domain.view.OperationTimeView;
import click.porito.managed_travel.place.domain.view.UserPlaceView;
import click.porito.managed_travel.place.place_service.repository.jpa.entity.*;
import click.porito.managed_travel.place.place_service.repository.jpa.repository.*;
import click.porito.managed_travel.place.place_service.util.AccountContextStrategy;
import click.porito.managed_travel.place.place_service.util.ValidatedArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static click.porito.managed_travel.place.place_service.util.GeoUtils.geoJsonPointToJtsPointMapper;
import static click.porito.managed_travel.place.place_service.util.GeoUtils.geoJsonPolygonToJtsPolygonMapper;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
@RequiredArgsConstructor
public class PlaceCommandApiImpl implements PlaceCommandApi {
    private final AccountContextStrategy accountContextStrategy;

    private final PlaceCategoryRepository placeCategoryRepository;
    private final AccountSnapshotRepository accountSnapshotRepository;
    private final UserPlaceRepository userPlaceRepository;
    private final OfficialPlaceRepository officialPlaceRepository;
    private final PlaceRepository placeRepository;


    @Override
    @ValidatedArgs(nullable = false)
    @PreAuthorize("isAuthenticated() and hasAuthority('user_place:put')")
    public UserPlaceView putUserPlace(UserPlacePutRequest request) {
        // 변환 및 준비
        final Point location;
        final Polygon boundary;
        try {
            location = geoJsonPointToJtsPointMapper().map(request.getLocation());
            boundary = geoJsonPolygonToJtsPolygonMapper().map(request.getBoundary());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid location or boundary");
        }

        AccountSnapshotEntity account = accountContextStrategy.getAccountId()
                .flatMap(accountSnapshotRepository::findAccountSnapshotEntitiesByAccountId)
                .orElseThrow(() -> new IllegalArgumentException("AccountSnapshot not found"));

        List<CategoryEntity> categoryEntities = placeCategoryRepository.findByCategoryIn(request.getCategories());
        if (categoryEntities.size() != request.getCategories().size()) {
            throw new IllegalArgumentException("Invalid categories");
        }

        // UserPlaceEntity 생성
        final UserPlaceEntity userPlaceEntity;
        if (request.getPlaceId() != null) {
            userPlaceEntity = userPlaceRepository.findByPlaceIdAndAccountIdForUpdate(request.getPlaceId(), account.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("UserPlace not found"));
        } else {
            userPlaceEntity = new UserPlaceEntity();
            userPlaceEntity.setAccountSnapshotEntity(account);
        }

        // UserPlaceEntity 설정
        userPlaceEntity.setName(request.getName());
        userPlaceEntity.setKeywords(request.getKeywords());
        userPlaceEntity.setAddress(request.getAddress());
        userPlaceEntity.setPostalCode(request.getPostalCode());
        userPlaceEntity.setPhoneNumber(request.getPhoneNumber());
        userPlaceEntity.setWebsite(request.getWebsite());
        userPlaceEntity.setSummary(request.getSummary());
        userPlaceEntity.setLocation(location);
        userPlaceEntity.setBoundary(boundary);
        userPlaceEntity.setCategories(categoryEntities);
        userPlaceEntity.setAccountSnapshotEntity(account);

        // UserPlaceEntity 저장
        UserPlaceEntity saved = userPlaceRepository.save(userPlaceEntity);
        // UserPlaceView 생성
        return UserPlaceEntity.toView(saved);
    }

    @Override
    @ValidatedArgs(nullable = false)
    @PreAuthorize("isAuthenticated() and hasAuthority('official_place:put')")
    public OfficialPlaceView putOfficialPlace(OfficialPlacePutRequest request) {
        // 변환 및 준비
        final Point location;
        final Polygon boundary;
        try {
            location = geoJsonPointToJtsPointMapper().map(request.getLocation());
            boundary = geoJsonPolygonToJtsPolygonMapper().map(request.getBoundary());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid location or boundary");
        }

        List<CategoryEntity> categoryEntities = placeCategoryRepository.findByCategoryIn(request.getCategories());
        if (categoryEntities.size() != request.getCategories().size()) {
            throw new IllegalArgumentException("Invalid categories");
        }

        // OfficialPlaceEntity 생성
        final OfficialPlaceEntity officialPlaceEntity;
        if (request.getPlaceId() != null) {
            officialPlaceEntity = officialPlaceRepository.findByPlaceIdForUpdate(request.getPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("OfficialPlace not found"));
        } else {
            officialPlaceEntity = new OfficialPlaceEntity();
        }

        // OfficialPlaceEntity 설정
        officialPlaceEntity.setName(request.getName());
        officialPlaceEntity.setKeywords(request.getKeywords());
        officialPlaceEntity.setAddress(request.getAddress());
        officialPlaceEntity.setPostalCode(request.getPostalCode());
        officialPlaceEntity.setPhoneNumber(request.getPhoneNumber());
        officialPlaceEntity.setWebsite(request.getWebsite());
        officialPlaceEntity.setSummary(request.getSummary());
        officialPlaceEntity.setLocation(location);
        officialPlaceEntity.setBoundary(boundary);
        officialPlaceEntity.setCategories(categoryEntities);
        officialPlaceEntity.setIsPublic(request.getIsPublic());
        officialPlaceEntity.setGooglePlaceId(request.getGooglePlaceId());

        // OfficialPlaceEntity 저장
        OfficialPlaceEntity saved = officialPlaceRepository.save(officialPlaceEntity);
        return OfficialPlaceEntity.toView(saved);
    }

    @Override
    @PreAuthorize("isAuthenticated() and hasAuthority('user_place:delete')")
    public void deletePlace(Long placeId) {
        Assert.notNull(placeId, "placeId is null");
        AccountSnapshotEntity account = accountContextStrategy.getAccountId()
                .flatMap(accountSnapshotRepository::findAccountSnapshotEntitiesByAccountId)
                .orElseThrow(() -> new IllegalArgumentException("AccountSnapshot not found"));
        UserPlaceEntity userPlaceEntity = userPlaceRepository.findByPlaceIdAndAccountIdForUpdate(placeId, account.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("UserPlace not found"));
        userPlaceRepository.delete(userPlaceEntity); // delete immediately
    }

    @Override
    @ValidatedArgs(nullable = false)
    @PreAuthorize("isAuthenticated()")
    public OperationTimeView putOperationTime(OperationTimePutRequest request) {
        // 변환 및 준비
        PlaceEntity placeEntity = placeRepository.findByPlaceId(request.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("OfficialPlace not found"));

        // 권한이 있는지 검증
        if (placeEntity instanceof UserPlaceEntity) {
            accountContextStrategy.hasAuthority("user_place:put");
        } else if (placeEntity instanceof OfficialPlaceEntity) {
            accountContextStrategy.hasAuthority("official_place:put");
        } else {
            throw new IllegalArgumentException("Invalid place type");
        }

        // OperationTimeEntity 생성
        final OperationTimeEntity operationTimeEntity;
        if (request.getOperationTimeId() != null) {
            operationTimeEntity = placeEntity.getOperationTimes().stream()
                    .filter(ote -> ote.getOperationTimeId().equals(request.getOperationTimeId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("OperationTime not found"));
        } else {
            operationTimeEntity = new OperationTimeEntity();
            placeEntity.getOperationTimes().add(operationTimeEntity);
            operationTimeEntity.setPlaceEntity(placeEntity);
        }

        // OperationTimeEntity 설정
        operationTimeEntity.setStartDate(request.getStartDate());
        operationTimeEntity.setEndDate(request.getEndDate());

        // Day들을 다 업데이트한다.
        for (DayOperationTimePutRequest putRequest : request.getDayOperationTimes()) {
            final DayOperationTimeEntity dayOperationTimeEntity;
            if (putRequest.getDayOperationTimeId() == null) {
                dayOperationTimeEntity = new DayOperationTimeEntity();
                dayOperationTimeEntity.setOperationTimeEntity(operationTimeEntity);
                operationTimeEntity.getDayOperationTimeEntities().add(dayOperationTimeEntity);
            } else {
                dayOperationTimeEntity = operationTimeEntity.getDayOperationTimeEntities().stream()
                        .filter(dot -> dot.getDayOperationTimeId().equals(putRequest.getDayOperationTimeId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("DayOperationTime not found"));
            }
            dayOperationTimeEntity.setStartTime(putRequest.getStartTime());
            dayOperationTimeEntity.setEndTime(putRequest.getEndTime());
            dayOperationTimeEntity.setNextDayLinked(putRequest.getNextDayLinked());
        }

        // place 저장 -> cascade persist
        PlaceEntity saved = placeRepository.save(placeEntity);

        // OperationTimeView 생성
        OperationTimeEntity savedOperationTimeEntity = saved.getOperationTimes().stream()
                .filter(ote -> ote.getOperationTimeId().equals(operationTimeEntity.getOperationTimeId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OperationTime not found"));


        return OperationTimeEntity.toView(savedOperationTimeEntity);
    }


}
