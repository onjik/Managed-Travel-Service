package click.porito.travel_core.plan;

/**
 * 여행 계획을 읽어 드리던 도중 문제가 발생했을 때 발생하는 예외
 */
public class PlanFetchException extends AbstractPlanServiceException{
    public PlanFetchException(String msg) {
        super(msg);
    }

    public PlanFetchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
