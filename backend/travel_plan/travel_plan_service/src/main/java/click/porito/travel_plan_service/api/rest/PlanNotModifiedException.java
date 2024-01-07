package click.porito.travel_plan_service.api.rest;

import jakarta.validation.constraints.NotBlank;

public class PlanNotModifiedException extends Throwable {
    public PlanNotModifiedException(@NotBlank String planId) {
    }
}
