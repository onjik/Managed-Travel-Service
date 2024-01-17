package click.porito.travel_core.plan.api.rest;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

public record PlanUpdateRequest(
        @Length(min = 1, max = 100, message = "title length must be between 1 and 100")
        @NotBlank(message = "title must not be blank")
        String title,

        @FutureOrPresent(message = "startDate must be present or future")
        LocalDate startDate,
        List<RouteComponentRequest> route
) {
}
