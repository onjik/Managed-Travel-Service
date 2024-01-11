package click.porito.account.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class OidcEmailNotVerifiedException extends AuthenticationException {
    private final String email;
    private String issuer;

    public OidcEmailNotVerifiedException(String email, String issuer) {
        super("Try to login with unverified email: " + email + " from " + issuer);
        this.email = email;
        this.issuer = issuer;
    }
}
