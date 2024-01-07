package click.porito.travel_plan_service.dto;

import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;

public record PlanView(
        String planId,
        String title,
        @Nullable
        LocalDate startDate,
        Instant createdAt,
        Instant updatedAt,
        String version,
        RouteComponentView[] route
) {
}
