package click.porito.account.global.exception;

import click.porito.account.global.constant.Domain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.NestedRuntimeException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public sealed abstract class ServerThrownException extends NestedRuntimeException permits ServerException, BusinessException {
    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private final Domain domain;
    private final ErrorCode errorCode;
    @Setter
    private Map<String, Object> detailsToExpose = new HashMap<>();
    @Setter
    private List<FieldError> fieldErrors = new ArrayList<>();
    public ServerThrownException(Domain domain, ErrorCode errorCode) {
        super("");
        this.domain = domain;
        this.errorCode = errorCode;
    }

    public ServerThrownException(Throwable cause, Domain domain, ErrorCode errorCode) {
        super("", cause);
        this.domain = domain;
        this.errorCode = errorCode;
    }

    public void addDetail(String key, Object value) {
        this.detailsToExpose.put(key, value);
    }

    public void addDetails(Map<String, Object> details) {
        this.detailsToExpose.putAll(details);
    }

    public void addFieldError(FieldError fieldError) {
        this.fieldErrors.add(fieldError);
    }

    public void addFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors.addAll(fieldErrors);
    }
}
