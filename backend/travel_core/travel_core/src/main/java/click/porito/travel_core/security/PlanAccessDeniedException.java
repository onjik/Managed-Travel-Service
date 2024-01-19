package click.porito.travel_core.security;

import click.porito.travel_core.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PlanAccessDeniedException extends SecurityBusinessException{

    public PlanAccessDeniedException(ErrorCode errorCode, PlanAccessManager.AccessType[] deniedOperations) {
        super(errorCode);
        super.addDetail("deniedOperations", deniedOperations);
    }

    public PlanAccessDeniedException(Throwable cause, ErrorCode errorCode, PlanAccessManager.AccessType[] deniedOperations) {
        super(cause, errorCode);
        super.addDetail("deniedOperations", deniedOperations);
    }
}
