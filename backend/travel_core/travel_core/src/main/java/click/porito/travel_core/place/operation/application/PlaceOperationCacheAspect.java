package click.porito.travel_core.place.operation.application;

import click.porito.travel_core.place.domain.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class PlaceOperationCacheAspect {
    private final PlaceCacheOperation placeCacheOperation;

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.Optional<click.porito.travel_core.place.domain.Place> click.porito.travel_core.place.operation.application.PlaceOperation.getPlace(String)) && args(placeId)")
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
    @Around("execution(java.util.List<click.porito.travel_core.place.domain.Place> click.porito.travel_core.place.operation.application.PlaceOperation.getPlaces(String[])) && args(placeIds)")
    public List<Place> getPlaces(ProceedingJoinPoint joinPoint, String[] placeIds) throws Throwable {
        List<Place> matchedCache = placeCacheOperation.getByIdIn(placeIds);
        if (matchedCache.size() == placeIds.length) {
            log.debug("Cache Hit - Get Places From Cache : {}", placeIds);
            return matchedCache;
        }
        String[] unmatchedPlaceIds;
        if (matchedCache.isEmpty()){
            log.debug("Cache Miss - Get Places From : {}", this.getClass().getSimpleName());
            unmatchedPlaceIds = placeIds;
        } else {
            log.debug("Cache Partial Hit ({} / {}) - Get Places From : {}", matchedCache.size(), placeIds.length, this.getClass().getSimpleName());
            Set<String> matchedIds = matchedCache.stream().map(Place::id).collect(Collectors.toSet());
            unmatchedPlaceIds = Arrays.stream(placeIds)
                    .filter(placeId -> !matchedIds.contains(placeId))
                    .toArray(String[]::new);
        }
        // 부족한 부분 조회
        List<Place> result = (List<Place>) joinPoint.proceed(new Object[]{unmatchedPlaceIds});
        if (result == null) {
            return matchedCache;
        }
        // load to cache
        if (!result.isEmpty()) {
            // cache
            log.debug("Cache Put - Put Places To Cache : {}", unmatchedPlaceIds);
            placeCacheOperation.putAll(result);
        }

        // merge and sort by placeIds
        result.addAll(matchedCache);
        List<String> placeIdList = Arrays.asList(placeIds);
        result.sort((o1, o2) -> {
            int o1Index = placeIdList.indexOf(o1.id());
            int o2Index = placeIdList.indexOf(o2.id());
            return Integer.compare(o1Index, o2Index);
        });

        return result;
    }

    @SuppressWarnings("unchecked")
    @Around("execution(java.util.List<click.porito.travel_core.place.domain.Place> click.porito.travel_core.place.operation.application.PlaceOperation.getPlaceNearBy(double, double, int, java.lang.Integer, click.porito.travel_core.place.domain.PlaceType[], java.lang.Boolean))")
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

    @Around("execution(boolean click.porito.travel_core.place.operation.application.PlaceOperation.exists(String)) && args(placeId)")
    public boolean aroundExists(ProceedingJoinPoint joinPoint, String placeId) throws Throwable {
        // check cache
        boolean exists = placeCacheOperation.exists(placeId);
        if (exists) {
            log.debug("Cache Hit - Exists PlaceEntity From Cache : {}", placeId);
            return true;
        }
        // cache put 은 진행하지 않는다(현재 구현체에서)
        return (boolean) joinPoint.proceed();
    }
}
