package click.porito.optimization_service.security.config;

import click.porito.security.autoconfigure.EnableJwtPermitAllSecurityChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@EnableJwtPermitAllSecurityChain
@Configuration
public class SecurityConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        //{domain}:{action}:{scope}
        //domain : place, plan
        //action : read, create, update, delete
        //scope : owned, belonged, all, new(only for *:create)
        String hierarchyString = """
                ROLE_ADMIN > ROLE_STAFF
                ROLE_STAFF > ROLE_USER
                ROLE_STAFF > place:read:all
                ROLE_STAFF > plan:read:all
                ROLE_USER > place:read:all
                ROLE_USER > plan:create:owned
                ROLE_USER > plan:read:owned
                ROLE_USER > plan:read:belonged
                ROLE_USER > plan:update:owned
                ROLE_USER > plan:update:belonged
                ROLE_USER > plan:delete:owned
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
