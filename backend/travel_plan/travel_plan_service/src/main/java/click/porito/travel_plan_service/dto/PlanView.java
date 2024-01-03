package click.porito.travel_plan_service.dto;

import java.time.Instant;
import java.time.LocalDate;

public record PlanView(
        String planId,
        String title,
        LocalDate startDate,
        Instant createdAt,
        Instant updatedAt,
        String version,
        RouteComponentView[] route
) {
}
