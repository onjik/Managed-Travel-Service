package click.porito.account_common.exception;


import click.porito.common.exception.ErrorCodes;

public class InsufficientRegistrationNotFoundException extends AccountBusinessException{
    public InsufficientRegistrationNotFoundException() {
        super(ErrorCodes.RESOURCE_NOT_FOUND);
    }
}
