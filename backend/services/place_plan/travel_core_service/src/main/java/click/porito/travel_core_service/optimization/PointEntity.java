package click.porito.travel_core_service.optimization;

import lombok.Builder;

@Builder
public record PointEntity(
        String id,
        double longitude,
        double latitude
){

}
