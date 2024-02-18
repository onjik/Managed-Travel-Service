package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector;

import click.porito.common.exception.ErrorResponseBody;
import click.porito.common.exception.common.ErrorCode;
import org.springframework.http.HttpStatusCode;

public record ErrorCodeMapper(
        String code,
        String debugDescription,
        int status
) implements ErrorCode {

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDebugDescription() {
        return debugDescription;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public static ErrorCodeMapper of(ErrorResponseBody body) {
        return new ErrorCodeMapper(
                body.getCode(),
                body.getDebugDescription(),
                body.getStatus()
        );
    }
}
