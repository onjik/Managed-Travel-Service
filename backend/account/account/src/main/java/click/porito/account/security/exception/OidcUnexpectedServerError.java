package click.porito.account.security.exception;

import org.springframework.security.core.AuthenticationException;

public class OidcUnexpectedServerError extends AuthenticationException {
    public OidcUnexpectedServerError() {
        super("Unexpected server error");
    }
}
