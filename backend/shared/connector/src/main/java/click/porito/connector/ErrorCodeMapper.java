package click.porito.connector;

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
    public HttpStatusCode getStatus() {
        return HttpStatusCode.valueOf(status);
    }

    public static ErrorCodeMapper of(ErrorResponseBody body) {
        return new ErrorCodeMapper(
                body.getCode(),
                body.getDebugDescription(),
                body.getStatus()
        );
    }
}
