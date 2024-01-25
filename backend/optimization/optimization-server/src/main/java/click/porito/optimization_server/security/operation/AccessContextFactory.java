package click.porito.optimization_server.security.operation;

import click.porito.optimization_server.security.domain.AccessContext;
import click.porito.optimization_server.security.domain.Authority;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.util.regex.Pattern;

@Getter
public class AccessContextFactory {
    private final static Pattern SCOPE_PATTERN = Pattern.compile("^SCOPE_(?<domain>.+):(?<action>.+)(?::(?<scope>.+))?$");
    private final static String ROLE_PREFIX = "ROLE_";
    private final AuthenticationTrustResolver trustResolver;
    private final RoleHierarchy roleHierarchy;

    @Builder
    public AccessContextFactory(AuthenticationTrustResolver trustResolver, RoleHierarchy roleHierarchy) {
        this.trustResolver = trustResolver != null ? trustResolver : new AuthenticationTrustResolverImpl();
        this.roleHierarchy = roleHierarchy != null ? roleHierarchy : new NullRoleHierarchy();
    }

    public AccessContext create(Authentication authentication) {
        return AccessContext.builder()
                .authentication(authentication)
                .authoritySet(Authority.parseAuthoritySet(authentication, roleHierarchy))
                .roleHierarchy(roleHierarchy)
                .trustResolver(trustResolver)
                .build();
    }

}
