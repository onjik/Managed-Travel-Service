package click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception;


import click.porito.common.exception.ErrorCodes;

public class JwtTokenExpiredException extends JwtBusinessException {
    public JwtTokenExpiredException() {
        super(ErrorCodes.JWT_EXPIRED);
    }

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCodes.JWT_EXPIRED);
    }
}
