package click.porito.account.security.exception;

import click.porito.account.account.api.request.AccountRegisterRequest;
import click.porito.account.security.component.DefaultLoginFailureHandler;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * 필요한 정보가 비어 있는 채로 회원가입을 시도할 경우 발생하는 예외(자동 회원가입의 상황도 포함한다.)
 * @see DefaultLoginFailureHandler
 */
@Getter
public class InsufficientRegisterInfoException extends AuthenticationException {
    private final AccountRegisterRequest registerForm;
    private final Set<ConstraintViolation<AccountRegisterRequest>> violations;
    public InsufficientRegisterInfoException(AccountRegisterRequest registerForm, Set<ConstraintViolation<AccountRegisterRequest>> violations) {
        super("Try to sign in with insufficient user info");
        Assert.notNull(registerForm, "registerForm must not be null");
        Assert.notNull(violations, "violations must not be null");
        Assert.notEmpty(violations, "violations must not be empty");
        this.violations = violations;
        this.registerForm = registerForm;
    }
}
