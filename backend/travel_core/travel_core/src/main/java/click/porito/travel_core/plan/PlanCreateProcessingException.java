package click.porito.travel_core.plan;

public class PlanCreateProcessingException extends PlanServerException{
    public PlanCreateProcessingException(String msg) {
        super(msg);
    }

    public PlanCreateProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
