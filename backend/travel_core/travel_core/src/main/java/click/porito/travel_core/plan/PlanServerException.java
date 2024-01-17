package click.porito.travel_core.plan;

public class PlanServerException extends AbstractPlanServiceException{
    public PlanServerException(String msg) {
        super(msg);
    }

    public PlanServerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
