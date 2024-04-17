package click.porito.managed_travel.place.place_service.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Component
@Aspect
@RequiredArgsConstructor
public class ValidatedArgsAspect {
    private final ValidatorFactory validatorFactory;

    // @ValidatedArgs 가 붙은 메서드 호출 전에 실행
    @Around("@annotation(validatedArgs)")
    public Object validateArgs(ProceedingJoinPoint joinPoint, ValidatedArgs validatedArgs) throws Throwable {
        Validator validator = validatorFactory.getValidator();
        // 메서드의 파라미터를 검증
        Object[] args = joinPoint.getArgs();
        boolean nullable = validatedArgs.nullable();

        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        for (Object arg : args) {
            if (arg == null) {
                if (!nullable) {
                    throw new IllegalArgumentException("Argument must not be null");
                }
                continue;
            }
            violations.addAll(validator.validate(arg));
        }

        // 검증 결과가 유효하지 않으면 예외를 던짐
        if (!violations.isEmpty()) {
            String joinedMessage = violations.stream()
                    .map(violation -> "%s : %s".formatted(violation.getPropertyPath(), violation.getMessage()))
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(joinedMessage);
        }

        return joinPoint.proceed();
    }

}
