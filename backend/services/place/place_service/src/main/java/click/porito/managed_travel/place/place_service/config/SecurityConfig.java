package click.porito.managed_travel.place.place_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        //{domain}:{action}:{scope}
        //domain : place, plan
        //action : read, create, update, delete
        //scope : owned, belonged, all, new(only for *:create)
        String hierarchyString = """
                ROLE_ADMIN > ROLE_STAFF
                ROLE_ADMIN > official_place:delete
                ROLE_STAFF > ROLE_USER
                ROLE_STAFF > official_place:put
                ROLE_USER > user_place:put
                ROLE_USER > user_place:delete
                """;
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(hierarchyString);
        return hierarchy;
    }

    //for method security
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }


}
