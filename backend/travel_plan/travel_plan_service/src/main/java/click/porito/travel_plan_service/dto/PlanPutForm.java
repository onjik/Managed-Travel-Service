package click.porito.travel_plan_service.dto;

import java.time.LocalDate;

public record PlanPutForm(
        String title,
        LocalDate startDate,
        String version,
        RouteComponentView[] route
) {
}
