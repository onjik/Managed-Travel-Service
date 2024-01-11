package click.porito.travel_core.optimization;

import java.util.List;

public interface RouteOptimizeService {

    /**
     * 위치를 기준으로 최적 경로를 만들어냅니다.
     * @param pointEntities 최적화를 진행할 엔티티
     * @return 최적화된 순서의 엔티티
     */
    List<PointEntity> optimizeSequenceByLocation(List<PointEntity> pointEntities);

}
