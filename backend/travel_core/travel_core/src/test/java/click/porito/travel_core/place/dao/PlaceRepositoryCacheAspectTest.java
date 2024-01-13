package click.porito.travel_core.place.dao;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.PlaceType;
import click.porito.travel_core.place.cache.PlaceCacheService;
import click.porito.travel_core.place.dao.google_api.GooglePlaceApi;
import click.porito.travel_core.place.dao.google_api.GooglePlacePhotoApi;
import click.porito.travel_core.place.dao.google_api.model.GooglePlace;
import click.porito.travel_core.place.dto.PlaceView;
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
@SpringBootTest(classes = {PlaceRepositoryCacheAspect.class, GooglePlaceRepository.class})
@Import(AnnotationAwareAspectJAutoProxyCreator.class) // activate aspect
class PlaceRepositoryCacheAspectTest {

    @MockBean
    private GooglePlaceApi googlePlaceApi;
    @MockBean
    private GooglePlacePhotoApi googlePlacePhotoApi;
    @MockBean
    private Mapper<GooglePlace, PlaceView> googlePlaceViewMapper;
    @MockBean
    private PlaceCacheService placeCacheService;
    @SpyBean
    private PlaceRepositoryCacheAspect cacheAspect;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    @DisplayName("getPlace 호출시 Aspect에 있는 캐시 로직 호출")
    void getPlaceWithCacheAspect() throws Throwable {
        // given
        String placeId = "placeId";

        // when
        placeRepository.getPlace(placeId);

        // then
        verify(cacheAspect, times(1)).aroundGetPlace(any(), any());
        verify(placeCacheService, times(1)).get(any());
    }

    @Test
    @DisplayName("getPlaces 호출시 Aspect에 있는 캐시 로직 호출")
    void getPlacesWithCacheAspect() throws Throwable {
        // given
        String[] placeIds = {"placeId1", "placeId2"};

        // when
        placeRepository.getPlaces(placeIds);

        // then
        verify(cacheAspect, times(1)).getPlaces(any(), any());
        verify(placeCacheService, times(1)).getByIdIn(any());
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
        placeRepository.getPlaceNearBy(lat, lng, radius, maxResultCount, placeTypes, distanceSort);

        // then
        verify(cacheAspect, times(1)).aroundGetNearbyPlaces(any());
    }



}