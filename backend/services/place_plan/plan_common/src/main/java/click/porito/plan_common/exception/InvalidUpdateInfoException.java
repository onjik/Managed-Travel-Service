package click.porito.plan_common.exception;

import click.porito.common.exception.FieldError;
import org.springframework.lang.Nullable;

import java.util.List;

import static click.porito.common.exception.ErrorCodes.INVALID_INPUT_VALUE;


public class InvalidUpdateInfoException extends PlanBusinessException {

    public InvalidUpdateInfoException(@Nullable List<FieldError> fieldErrors) {
        super(INVALID_INPUT_VALUE);
        super.addDetail("fieldErrors", fieldErrors);
    }

    public InvalidUpdateInfoException(Throwable cause, @Nullable List<FieldError> fieldErrors) {
        super(cause, INVALID_INPUT_VALUE);
        super.addDetail("fieldErrors", fieldErrors);
    }
}
