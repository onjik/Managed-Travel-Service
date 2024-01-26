package click.porito.security.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(JwtOperationAutoConfiguration.class)
public @interface EnableJwtOperation {
}
