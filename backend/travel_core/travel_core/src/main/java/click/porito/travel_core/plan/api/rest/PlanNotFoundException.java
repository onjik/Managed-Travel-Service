package click.porito.travel_core.plan.api.rest;

import lombok.Getter;

/**
 * 지정한 여행 계획이 없는 경우, 발생하는 예외
 */
@Getter
public class PlanNotFoundException extends RuntimeException{
    private final String planId;
    public PlanNotFoundException(String planId) {
        this.planId = planId;
    }


}
