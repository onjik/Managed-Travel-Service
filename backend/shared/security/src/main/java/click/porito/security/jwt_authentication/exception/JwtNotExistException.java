package click.porito.security.jwt_authentication.exception;


import click.porito.common.exception.ErrorCodes;
import click.porito.security.SecurityBusinessException;

public class JwtNotExistException extends SecurityBusinessException {
    public JwtNotExistException() {
        super(ErrorCodes.JWT_NOT_EXIST);
    }
}
