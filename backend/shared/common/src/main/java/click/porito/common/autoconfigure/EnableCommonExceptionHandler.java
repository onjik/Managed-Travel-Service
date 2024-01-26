package click.porito.common.autoconfigure;


import click.porito.common.exception.CommonExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CommonExceptionHandler.class)
public @interface EnableCommonExceptionHandler {
}
