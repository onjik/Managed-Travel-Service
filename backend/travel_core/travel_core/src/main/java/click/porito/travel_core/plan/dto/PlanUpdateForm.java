package click.porito.travel_core.plan.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PlanUpdateForm(
        @Length(min = 1, max = 100, message = "title length must be between 1 and 100")
        @NotBlank(message = "title must not be blank")
        String title,
        @FutureOrPresent(message = "startDate must be present or future")
        LocalDate startDate,
        @NotBlank(message = "userId must not be blank")
        String userId,
        @Nullable
        String version,
        List<RouteComponentUpdateForm> route
) {
}
