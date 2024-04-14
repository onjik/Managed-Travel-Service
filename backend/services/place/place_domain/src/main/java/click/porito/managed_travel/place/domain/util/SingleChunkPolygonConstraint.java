package click.porito.managed_travel.place.domain.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SingleChunkPolygonValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleChunkPolygonConstraint {
    String message() default "Polygon must have only one chunk.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
