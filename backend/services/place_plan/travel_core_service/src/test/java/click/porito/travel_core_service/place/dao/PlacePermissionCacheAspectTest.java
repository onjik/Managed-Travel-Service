package click.porito.travel_core_service.place.dao;

import click.porito.common.util.Mapper;
import click.porito.place_common.domain.Place;
import click.porito.place_common.domain.PlaceType;
import click.porito.travel_core_service.place.operation.adapter.GooglePlaceOperation;
import click.porito.travel_core_service.place.operation.adapter.google_api.GooglePlaceApi;
import click.porito.travel_core_service.place.operation.adapter.google_api.GooglePlacePhotoApi;
import click.porito.travel_core_service.place.operation.adapter.google_api.model.GooglePlace;
import click.porito.travel_core_service.place.operation.application.PlaceCacheOperation;
import click.porito.travel_core_service.place.operation.application.PlaceOperation;
import click.porito.travel_core_service.place.operation.application.PlaceOperationCacheAspect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PlaceOperationCacheAspect.class, GooglePlaceOperation.class})
@Import(AnnotationAwareAspectJAutoProxyCreator.class) // activate aspect
class PlacePermissionCacheAspectTest {

    @MockBean
    private GooglePlaceApi googlePlaceApi;
    @MockBean
    private GooglePlacePhotoApi googlePlacePhotoApi;
    @MockBean
    private Mapper<GooglePlace, Place> googlePlaceViewMapper;
    @MockBean
    private PlaceCacheOperation placeCacheOperation;
    @SpyBean
    private PlaceOperationCacheAspect cacheAspect;

    @Autowired
    private PlaceOperation placeOperation;

    @Test
    @DisplayName("getPlace 호출시 Aspect에 있는 캐시 로직 호출")
    void getPlaceWithCacheAspect() throws Throwable {
        // given
        String placeId = "placeId";

        // when
        placeOperation.getPlace(placeId);

        // then
        verify(cacheAspect, times(1)).aroundGetPlace(any(), any());
        verify(placeCacheOperation, times(1)).get(any());
    }

    @Test
    @DisplayName("getPlaces 호출시 Aspect에 있는 캐시 로직 호출")
    void getPlacesWithCacheAspect() throws Throwable {
        // given
        String[] placeIds = {"placeId1", "placeId2"};

        // when
        placeOperation.getPlaces(placeIds);

        // then
        verify(cacheAspect, times(1)).getPlaces(any(), any());
        verify(placeCacheOperation, times(1)).getByIdIn(any());
    }

    @Test
    @DisplayName("getPlaceNearBy 호출시 Aspect에 있는 캐시 로직 호출")
    void getPlaceNearByWithCacheAspect() throws Throwable {
        // given
        double lat = 1.0;
        double lng = 1.0;
        int radius = 1000;
        Integer maxResultCount = 10;
        PlaceType[] placeTypes = {PlaceType.RESTAURANT};
        Boolean distanceSort = true;

        // when
        placeOperation.getPlaceNearBy(lat, lng, radius, maxResultCount, placeTypes, distanceSort);

        // then
        verify(cacheAspect, times(1)).aroundGetNearbyPlaces(any());
    }



}