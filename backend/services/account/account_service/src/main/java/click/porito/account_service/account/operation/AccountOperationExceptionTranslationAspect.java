package click.porito.account_service.account.operation;

import click.porito.account_common.exception.AccountServerException;
import click.porito.common.exception.ErrorCodes;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AccountOperationExceptionTranslationAspect {

    @Around("execution(* click.porito.account_service.account.operation.AccountOperation.*(..))")
    public Object translate(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            var exception = new AccountServerException(ErrorCodes.ACCOUNT_DATABASE_EXCEPTION);
            exception.addDetail("message", e.getMessage());
            throw exception;
        }
    }
}
