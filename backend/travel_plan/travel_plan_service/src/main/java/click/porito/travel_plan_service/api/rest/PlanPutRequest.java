package click.porito.travel_plan_service.api.rest;

import click.porito.travel_plan_service.dto.PlanPutForm;
import click.porito.travel_plan_service.dto.RouteComponentView;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record PlanPutRequest(
        @NotBlank(message = "title must not be blank")
        String title,
        LocalDate startDate,
        RouteComponentView[] route
) {

        PlanPutForm toPlanPutForm(@Nullable String version) {
                return new PlanPutForm(title(), startDate(), version, route());
        }
}
