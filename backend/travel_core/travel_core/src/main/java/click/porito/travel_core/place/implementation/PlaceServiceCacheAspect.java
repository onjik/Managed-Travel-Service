package click.porito.travel_core.place.implementation;

import click.porito.travel_core.place.cache.PlaceCacheService;
import click.porito.travel_core.place.dto.PlaceView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Aspect
@Slf4j
@Component
public class PlaceServiceCacheAspect {
    private final PlaceCacheService placeCacheService;

    public PlaceServiceCacheAspect(PlaceCacheService placeCacheService) {
        this.placeCacheService = placeCacheService;
    }

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.Optional<click.porito.travel_core.place.dto.PlaceView> click.porito.travel_core.place.PlaceService.getPlace(String)) && args(placeId)")
    public Object aroundGetPlace(ProceedingJoinPoint joinPoint, String placeId) throws Throwable {
        // check cache
        Optional<PlaceView> placeView = placeCacheService.get(placeId);
        if (placeView.isPresent()) {
            log.debug("Cache Hit - Get Place From Cache : {}", placeId);
            return placeView;
        }

        // cache miss
        log.debug("Cache Miss - Get Place From : {}", joinPoint.getTarget().getClass().getSimpleName());
        Optional<PlaceView> result = (Optional<PlaceView>) joinPoint.proceed();

        // load to cache
        if (result.isPresent()) {
            // cache
            log.debug("Cache Put - Put Place To Cache : {}", placeId);
            placeCacheService.put(result.get());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.List<click.porito.travel_core.place.dto.PlaceView> click.porito.travel_core.place.PlaceService.getNearbyPlaces(double, double, int, java.lang.Integer, click.porito.travel_core.place.PlaceType[], java.lang.Boolean))")
    public Object aroundGetNearbyPlaces(ProceedingJoinPoint joinPoint) throws Throwable {
        // 타겟 메서드 실행
        List<PlaceView> result = (List<PlaceView>) joinPoint.proceed();
        // cache all
        if (result != null && !result.isEmpty()){
            placeCacheService.putAll(result);
        }

        return result;
    }
}
