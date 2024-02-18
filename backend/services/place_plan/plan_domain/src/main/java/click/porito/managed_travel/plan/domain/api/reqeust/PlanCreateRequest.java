package click.porito.managed_travel.plan.domain.api.reqeust;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record PlanCreateRequest(
        @Length(min = 1, max = 100, message = "title length must be between 1 and 100")
        @NotBlank(message = "title must not be blank")
        String title,

        @FutureOrPresent(message = "startDate must be present or future")
        LocalDate startDate
) {
}
