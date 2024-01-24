package click.porito.account.account.exception;

import click.porito.account.global.exception.ErrorCode;

public class InsufficientRegistrationNotFoundException extends AccountBusinessException{
    public InsufficientRegistrationNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
