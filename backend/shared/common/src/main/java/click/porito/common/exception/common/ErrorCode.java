package click.porito.common.exception.common;

import org.springframework.http.HttpStatusCode;

public interface ErrorCode {
    HttpStatusCode getStatus();
    String getCode();
    String getDebugDescription();
}
