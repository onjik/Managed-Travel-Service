package click.porito.travel_core.place.implementation;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.PlaceService;
import click.porito.travel_core.place.cache.PlaceCacheService;
import click.porito.travel_core.place.dao.GooglePlacePhotoRepository;
import click.porito.travel_core.place.dao.GooglePlaceRepository;
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
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PlaceServiceCacheAspect.class,PlacePhotoServiceImpl.class})
@Import(AnnotationAwareAspectJAutoProxyCreator.class) // activate aspect
class PlaceServiceCacheAspectTest {



    @MockBean
    private PlaceCacheService placeCacheService;

    @SpyBean
    private PlaceServiceCacheAspect cacheAspect;

    @MockBean
    private GooglePlacePhotoRepository googlePlacePhotoRepository;
    @MockBean
    private GooglePlaceRepository googlePlaceRepository;

    @MockBean
    private Mapper<GooglePlace, PlaceView> googlePlaceViewMapper;

    @Autowired
    private PlaceService placeService;

    @Test
    @DisplayName("getPlace 호출시 Aspect에 있는 캐시 로직 호출")
    void getPlaceWithCacheAspect() throws Throwable {
        // given
        String placeId = "placeId";

        // when
        Optional<PlaceView> place = placeService.getPlace(placeId);

        // then
        //호출되었는지 체크
        verify(placeCacheService, times(1)).get(any());
        verify(cacheAspect, times(1)).aroundGetPlace(any(), any());
    }

    @Test
    @DisplayName("getNearbyPlaces 호출시 Aspect에 있는 캐시 로직 호출")
    void getNearbyPlacesWithCacheAspect() throws Throwable {
        // given
        double lat = 0;
        double lng = 0;
        int radius = 0;

        // when
        placeService.getNearbyPlaces(lat, lng, radius, null, null, null);

        // then
        //호출되었는지 체크
        verify(cacheAspect, times(1)).aroundGetNearbyPlaces(any());
    }


}