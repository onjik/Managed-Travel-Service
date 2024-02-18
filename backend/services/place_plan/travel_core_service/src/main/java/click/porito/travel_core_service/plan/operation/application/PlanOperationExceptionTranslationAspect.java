package click.porito.travel_core_service.plan.operation.application;

import click.porito.managed_travel.plan.domain.exception.PlanOperationFailedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PlanOperationExceptionTranslationAspect {

    @Around("execution(* click.porito.travel_core_service.plan.operation.application.PlanOperation.*(..))")
    public Object translate(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            throw new PlanOperationFailedException(e);
        }
    }
}
