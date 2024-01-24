package click.porito.account.account.operation;

import click.porito.account.account.exception.AccountServerException;
import click.porito.account.global.exception.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AccountOperationExceptionTranslationAspect {

    @Around("execution(* click.porito.account.account.operation.AccountOperation.*(..))")
    public Object translate(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            var exception = new AccountServerException(ErrorCode.ACCOUNT_DATABASE_EXCEPTION);
            exception.addDetail("message", e.getMessage());
            throw exception;
        }
    }
}
