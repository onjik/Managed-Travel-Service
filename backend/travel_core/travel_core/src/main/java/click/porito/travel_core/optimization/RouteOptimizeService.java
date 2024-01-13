package click.porito.travel_core.optimization;

import java.util.List;

public interface RouteOptimizeService {

    /**
     * 위치를 기준으로 최적 경로를 만들어냅니다.
     * @param pointEntities 최적화를 진행할 엔티티
     * @return 최적화된 순서의 엔티티
     */
    List<PointEntity> reorderByDistance(List<PointEntity> pointEntities) throws OptimizationProcessingException;


    /**
     * 위치를 기준으로 최적 경로를 만들어냅니다. 날짜 수를 지정할 수 있습니다.
     * @param pointEntities 최적화를 진행할 엔티티
     * @param dayCount 날짜 수
     * @return 최적화된 순서의 엔티티
     */
    List<List<PointEntity>> reorderByDistanceAndDayCount(List<PointEntity> pointEntities, int dayCount) throws OptimizationProcessingException;

}
