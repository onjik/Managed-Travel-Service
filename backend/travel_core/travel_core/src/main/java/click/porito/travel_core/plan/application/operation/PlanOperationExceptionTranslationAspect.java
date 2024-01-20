package click.porito.travel_core.plan.application.operation;

import click.porito.travel_core.plan.PlanOperationFailedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PlanOperationExceptionTranslationAspect {

    @Around("execution(* click.porito.travel_core.plan.application.operation.PlanOperation.*(..))")
    public Object translate(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            throw new PlanOperationFailedException(e);
        }
    }
}
