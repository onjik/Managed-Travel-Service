package click.porito.managed_travel.domain.exception;


import click.porito.common.exception.ErrorCodes;

public class InsufficientRegistrationNotFoundException extends AccountBusinessException{
    public InsufficientRegistrationNotFoundException() {
        super(ErrorCodes.RESOURCE_NOT_FOUND);
    }
}
