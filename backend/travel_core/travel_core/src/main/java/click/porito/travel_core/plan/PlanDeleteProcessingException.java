package click.porito.travel_core.plan;

public class PlanDeleteProcessingException extends PlanServerException{
    public PlanDeleteProcessingException(String msg) {
        super(msg);
    }

    public PlanDeleteProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
