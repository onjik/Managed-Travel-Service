package click.porito.gateway.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {


//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder, JwtAuthorizationGatewayFilterFactory jwtAuthFactory){
//        return builder.routes()
//                .route("login_service", r -> r
//                        .path("/account/login/**", "/account/register/**", "/login/oauth2/code/**")
//                        .uri("http://localhost:29090")
//                )
//                .route("account_service", r -> r
//                        .path("/account/**", "/users/**")
//                        .filters(f -> f
//                                .filter(jwtAuthFactory.apply(
//                                        new JwtAuthorizationGatewayFilterFactory.Config(
//                                                new String[]{"ADMIN", "USER"}
//                                        )
//                                ))
//                        )
//                        .uri("http://localhost:29090")
//                )
//                .route("place_service", r -> r
//                        .path("/place/**")
//                        .filters(f -> f
//                                .filter(jwtAuthFactory.apply(
//                                        new JwtAuthorizationGatewayFilterFactory.Config(
//                                                new String[]{"USER", "ADMIN"}
//                                        )
//                                ))
//                        )
//                        .uri("http://localhost:29091")
//                )
//                .build();
//    }
}
