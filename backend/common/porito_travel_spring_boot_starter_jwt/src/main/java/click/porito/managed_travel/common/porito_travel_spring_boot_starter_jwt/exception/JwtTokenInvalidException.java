package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception;


import click.porito.common.exception.ErrorCodes;

public class JwtTokenInvalidException extends JwtBusinessException {
    public JwtTokenInvalidException() {
        super(ErrorCodes.JWT_INVALID);
    }

    public JwtTokenInvalidException(Throwable cause) {
        super(cause, ErrorCodes.JWT_INVALID);
    }
}
