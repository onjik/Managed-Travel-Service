package click.porito.travel_core.plan;

import lombok.Getter;

import static click.porito.travel_core.global.exception.ErrorCode.RESOURCE_NOT_FOUND;

/**
 * 지정한 여행 계획이 없는 경우, 발생하는 예외
 */
@Getter
public class PlanNotFoundException extends PlanBusinessException{
    public PlanNotFoundException(String planId) {
        super(RESOURCE_NOT_FOUND);
        super.addDetail("planId", planId);
    }

    public PlanNotFoundException(Throwable cause, String planId) {
        super(cause, RESOURCE_NOT_FOUND);
        super.addDetail("planId", planId);
    }
}
