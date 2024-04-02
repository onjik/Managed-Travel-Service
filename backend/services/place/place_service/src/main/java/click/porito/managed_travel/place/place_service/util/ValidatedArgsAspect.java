package click.porito.managed_travel.place.place_service.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Aspect
@RequiredArgsConstructor
public class ValidatedArgsAspect {
    private final ValidatorFactory validatorFactory;

    // @ValidatedArgs 가 붙은 메서드 호출 전에 실행
    @Around("@annotation(click.porito.managed_travel.place.place_service.util.ValidatedArgs)")
    public Object validateArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        Validator validator = validatorFactory.getValidator();
        // 메서드의 파라미터를 검증
        Object[] args = joinPoint.getArgs();

        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        for (Object arg : args) {
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
