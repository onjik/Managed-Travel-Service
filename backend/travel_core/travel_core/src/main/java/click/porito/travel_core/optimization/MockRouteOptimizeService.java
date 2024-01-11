package click.porito.travel_core.optimization;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockRouteOptimizeService implements RouteOptimizeService{
    @Override
    public List<PointEntity> optimizeSequenceByLocation(List<PointEntity> pointEntities) {
        return pointEntities;
    }
}
