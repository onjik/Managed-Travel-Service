package click.porito.modular_travel.security.exception;

import org.springframework.security.core.AuthenticationException;

public class OidcUnexpectedServerError extends AuthenticationException {
    public OidcUnexpectedServerError() {
        super("Unexpected server error");
    }
}
