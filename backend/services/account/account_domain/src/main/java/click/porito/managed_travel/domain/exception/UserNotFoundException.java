package click.porito.managed_travel.domain.exception;


import click.porito.common.exception.ErrorCodes;

/**
 * 요청한 유저가 존재하지 않을 때 발생하는 예외
 */
public class UserNotFoundException extends AccountBusinessException {
    public UserNotFoundException() {
        super(ErrorCodes.ACCOUNT_NOT_FOUND);
    }
}
