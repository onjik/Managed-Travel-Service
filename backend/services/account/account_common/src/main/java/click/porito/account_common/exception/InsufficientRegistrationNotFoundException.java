package click.porito.account_common.exception;


import click.porito.common.exception.ErrorCode;

public class InsufficientRegistrationNotFoundException extends AccountBusinessException{
    public InsufficientRegistrationNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
