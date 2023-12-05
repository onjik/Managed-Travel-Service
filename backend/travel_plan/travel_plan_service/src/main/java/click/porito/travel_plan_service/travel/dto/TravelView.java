package click.porito.travel_plan_service.travel.dto;

import java.time.Instant;

public record TravelView(
        TravelMetadata metadata,
        Plan plan
){
    public record TravelMetadata(
            long id,
            Instant createdAt,
            Instant updatedAt,
            String ownerId
    ){}
}
