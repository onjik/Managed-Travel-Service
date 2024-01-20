package click.porito.travel_core.place.application.operation;

import click.porito.travel_core.place.domain.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class PlaceOperationCacheAspect {
    private final PlaceCacheOperation placeCacheOperation;

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.Optional<click.porito.travel_core.place.domain.Place> click.porito.travel_core.place.application.operation.PlaceOperation.getPlace(String)) && args(placeId)")
    public Object aroundGetPlace(ProceedingJoinPoint joinPoint, String placeId) throws Throwable {
        // check cache
        Optional<Place> placeView = placeCacheOperation.get(placeId);
        if (placeView.isPresent()) {
            log.debug("Cache Hit - Get PlaceEntity From Cache : {}", placeId);
            return placeView;
        }

        // cache miss
        log.debug("Cache Miss - Get PlaceEntity From : {}", joinPoint.getTarget().getClass().getSimpleName());
        Optional<Place> result = (Optional<Place>) joinPoint.proceed();

        // load to cache
        if (result.isPresent()) {
            // cache
            log.debug("Cache Put - Put PlaceEntity To Cache : {}", placeId);
            placeCacheOperation.put(result.get());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.List<click.porito.travel_core.place.domain.Place> click.porito.travel_core.place.application.operation.PlaceOperation.getPlaces(String[])) && args(placeIds)")
    public List<Place> getPlaces(ProceedingJoinPoint joinPoint, String[] placeIds) throws Throwable {
        List<Place> matchedCache = placeCacheOperation.getByIdIn(placeIds);
        if (matchedCache.size() == placeIds.length) {
            log.debug("Cache Hit - Get Places From Cache : {}", placeIds);
            return matchedCache;
        }
        if (log.isDebugEnabled()) {
            if (matchedCache.isEmpty()){
                log.debug("Cache Miss - Get Places From : {}", this.getClass().getSimpleName());
            } else {
                log.debug("Cache Partial Hit ({} / {}) - Get Places From : {}", matchedCache.size(), placeIds.length, this.getClass().getSimpleName());
            }
        }
        List<Place> result = (List<Place>) joinPoint.proceed();
        // load to cache
        if (result != null && !result.isEmpty()) {
            // cache
            log.debug("Cache Put - Put Places To Cache : {}", placeIds);
            placeCacheOperation.putAll(result);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.List<click.porito.travel_core.place.domain.Place> click.porito.travel_core.place.application.operation.PlaceOperation.getPlaceNearBy(double, double, int, java.lang.Integer, click.porito.travel_core.place.PlaceType[], java.lang.Boolean))")
    public Object aroundGetNearbyPlaces(ProceedingJoinPoint joinPoint) throws Throwable {
        //TODO 파라미터 기반 결과 캐싱

        // 타겟 메서드 실행
        List<Place> result = (List<Place>) joinPoint.proceed();
        // cache all
        if (result != null && !result.isEmpty()){
            placeCacheOperation.putAll(result);
        }

        return result;
    }
}
