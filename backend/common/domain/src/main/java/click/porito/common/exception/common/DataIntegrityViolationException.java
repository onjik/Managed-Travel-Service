package click.porito.common.exception.common;

import click.porito.common.exception.Domain;
import click.porito.common.exception.ErrorCodes;

import java.util.List;

public class DataIntegrityViolationException extends CommonBusinessException{
    private List<String> violationMessages;


    public DataIntegrityViolationException(Domain domain) {
        super(domain, ErrorCodes.DATA_INTEGRITY_VIOLATION);
    }

    public DataIntegrityViolationException(Throwable cause, Domain domain) {
        super(cause, domain, ErrorCodes.DATA_INTEGRITY_VIOLATION);
    }
}
