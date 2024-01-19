package click.porito.travel_core.plan;

import click.porito.travel_core.global.exception.FieldError;
import org.springframework.lang.Nullable;

import java.util.List;

import static click.porito.travel_core.global.exception.ErrorCode.INVALID_INPUT_VALUE;

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
