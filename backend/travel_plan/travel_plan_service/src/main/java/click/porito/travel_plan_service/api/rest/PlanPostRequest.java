package click.porito.travel_plan_service.api.rest;

import click.porito.travel_plan_service.dto.PlanCreateForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Builder
public record PlanPostRequest(
        @Length(min = 1, max = 100, message = "title length must be between 1 and 100")
        @NotBlank(message = "title must not be blank")
        String title,
        @NotNull
        @Range(min = 1, max = 100, message = "dayCount must be between 1 and 100")
        Integer dayCount,
        LocalDate startDate,
        String[] placeIds
) {
        PlanCreateForm toPlanCreateForm(String ownerId) {
                return PlanCreateForm.builder()
                        .title(title())
                        .dayCount(dayCount())
                        .startDate(startDate())
                        .placeIds(placeIds())
                        .ownerId(ownerId)
                        .build();
        }
}