package click.porito.plan_common.exception;


import static click.porito.common.exception.ErrorCode.PLAN_OUT_OF_DATE;

/**
 * PlanEntity 의 버전이 일치하지 않을 때 발생하는 예외
 */
public class PlanVersionOutOfDateException extends PlanBusinessException{
    public PlanVersionOutOfDateException(String planId) {
        super(PLAN_OUT_OF_DATE);
        super.addDetail("planId", planId);
    }

    public PlanVersionOutOfDateException(Throwable cause, String planId) {
        super(cause, PLAN_OUT_OF_DATE);
        super.addDetail("planId", planId);
    }
}
