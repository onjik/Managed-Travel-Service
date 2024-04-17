package click.porito.managed_travel.place.place_service.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * 메서드의 인자를 검증한다. <br>
 * nullable 이 false 이면 null이 아닌지 검증한다. <br>
 * 검증에 실패한 경우 IllegalArgumentException 을 던진다.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedArgs {
    boolean nullable() default false;
}
