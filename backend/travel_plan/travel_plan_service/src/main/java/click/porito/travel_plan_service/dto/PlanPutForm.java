package click.porito.travel_plan_service.dto;

import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record PlanPutForm(
        String title,
        LocalDate startDate,
        @Nullable
        String version,
        RouteComponentView[] route
) {
}
