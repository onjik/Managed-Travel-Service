package click.porito.travel_core_service.optimization;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockRouteOptimizeApi implements RouteOptimizeApi {

    @Override
    public List<PointEntity> reorderByDistance(List<PointEntity> pointEntities) throws OptimizationProcessingException {
        return pointEntities;
    }

    @Override
    public List<List<PointEntity>> reorderByDistanceAndDayCount(List<PointEntity> pointEntities, int dayCount) throws OptimizationProcessingException {
        return List.of(pointEntities);
    }
}
