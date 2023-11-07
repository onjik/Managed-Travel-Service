package click.porito.modular_travel.security.exception;

import click.porito.modular_travel.security.component.OidcLoginFailureHandler;
import click.porito.modular_travel.account.AccountRegisterDTO;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * 필요한 정보가 비어 있는 채로 회원가입을 시도할 경우 발생하는 예외(자동 회원가입의 상황도 포함한다.)
 * @see OidcLoginFailureHandler
 */
@Getter
public class OidcInsufficientUserInfoException extends AuthenticationException {
    private final OidcUserRequest userRequest;
    private final AccountRegisterDTO registerForm;
    private final Set<ConstraintViolation<AccountRegisterDTO>> violations;
    public OidcInsufficientUserInfoException(OidcUserRequest userRequest, AccountRegisterDTO registerForm, Set<ConstraintViolation<AccountRegisterDTO>> violations) {
        super("Try to sign in with insufficient user info");
        Assert.notNull(registerForm, "registerForm must not be null");
        Assert.notNull(userRequest, "userRequest must not be null");
        Assert.notNull(violations, "violations must not be null");
        Assert.notEmpty(violations, "violations must not be empty");
        this.userRequest = userRequest;
        this.violations = violations;
        this.registerForm = registerForm;
    }
}
