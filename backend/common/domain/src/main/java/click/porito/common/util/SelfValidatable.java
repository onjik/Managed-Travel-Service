package click.porito.common.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public abstract class SelfValidatable<T> {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator;

    protected SelfValidatable() {
        this.validator = factory.getValidator();
    }

    @SuppressWarnings("unchecked")
    protected ValidateResult<T> validateSelf() {
        return new ValidateResult<>(validator.validate((T) this));
    }

    public static class ValidateResult<T>{
        private final Set<ConstraintViolation<T>> violations;

        public ValidateResult(Set<ConstraintViolation<T>> violations) {
            if (violations == null) {
                throw new IllegalArgumentException("violations must not be null");
            }
            this.violations = violations;
        }

        public boolean isValid() {
            return !violations.isEmpty();
        }

        public Set<ConstraintViolation<T>> getViolations() {
            return violations;
        }
    }
}
