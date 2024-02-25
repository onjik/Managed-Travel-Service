package click.porito.managed_travel.place.place_service.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.ClassNameTypeResolver;

@Configuration(proxyBeanMethods = false)
public class GraphQlConfig {

    @Bean
    public GraphQlSourceBuilderCustomizer sourceBuilderCustomizer() {
        var resolver = new ClassNameTypeResolver();
        return builder ->{
            builder.defaultTypeResolver(resolver);
            builder.configureRuntimeWiring(
                    runtimeWiring -> runtimeWiring
                            .scalar(ExtendedScalars.NonNegativeInt)
                            .scalar(ExtendedScalars.CountryCode)
                            .scalar(ExtendedScalars.Locale)
            );
        };
    }

}
