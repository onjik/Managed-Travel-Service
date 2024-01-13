package click.porito.travel_core.plan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Builder
public record PlanCreateForm(
        @Length(min = 1, max = 100, message = "title length must be between 1 and 100")
        @NotBlank(message = "title must not be blank")
        String title,
        @Range(min = 1, max = 100, message = "dayCount must be between 1 and 100")
        Integer dayCount,
        LocalDate startDate,
        String[] placeIds,
        @NotBlank(message = "ownerId must not be blank")
        String ownerId
) {
}
