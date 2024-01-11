package click.porito.travel_core.plan.dto;

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
