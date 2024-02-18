package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector;

import click.porito.common.exception.common.ErrorCode;
import org.springframework.http.HttpStatusCode;

public class StatusOnlyErrorCode implements ErrorCode {
    private final int status;

    public StatusOnlyErrorCode(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return "COMMON_0000";
    }

    @Override
    public String getDebugDescription() {
        return HttpStatusCode.valueOf(status).toString();
    }
}
