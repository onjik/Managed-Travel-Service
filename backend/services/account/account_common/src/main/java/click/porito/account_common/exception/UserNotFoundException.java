package click.porito.account_common.exception;


import click.porito.common.exception.ErrorCode;

/**
 * 요청한 유저가 존재하지 않을 때 발생하는 예외
 */
public class UserNotFoundException extends AccountBusinessException {
    public UserNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
