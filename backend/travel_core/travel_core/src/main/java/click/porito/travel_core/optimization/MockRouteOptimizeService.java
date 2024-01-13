package click.porito.travel_core.optimization;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockRouteOptimizeService implements RouteOptimizeService{

    @Override
    public List<PointEntity> reorderByDistance(List<PointEntity> pointEntities) throws OptimizationProcessingException {
        return pointEntities;
    }

    @Override
    public List<List<PointEntity>> reorderByDistanceAndDayCount(List<PointEntity> pointEntities, int dayCount) throws OptimizationProcessingException {
        return List.of(pointEntities);
    }
}
