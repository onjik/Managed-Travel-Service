package click.porito.travel_plan_service.dto;

import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Builder
public record PlanView(
        String planId,
        String title,
        @Nullable
        LocalDate startDate,
        Instant createdAt,
        Instant updatedAt,
        String version,
        List<RouteComponentView> route
) {
}
