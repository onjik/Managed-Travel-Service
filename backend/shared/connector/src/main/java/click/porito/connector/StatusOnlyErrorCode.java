package click.porito.connector;

import click.porito.common.exception.common.ErrorCode;
import org.springframework.http.HttpStatusCode;

public class StatusOnlyErrorCode implements ErrorCode {
    private final HttpStatusCode status;

    public StatusOnlyErrorCode(HttpStatusCode status) {
        this.status = status;
    }

    @Override
    public HttpStatusCode getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return "COMMON_0000";
    }

    @Override
    public String getDebugDescription() {
        return status.toString();
    }
}
