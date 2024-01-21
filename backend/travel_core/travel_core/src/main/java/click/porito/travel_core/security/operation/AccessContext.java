package click.porito.travel_core.security.operation;

import click.porito.travel_core.security.domain.Authority;
import click.porito.travel_core.security.domain.RoleAuthority;
import click.porito.travel_core.security.domain.PermissionAuthority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import java.util.Set;
import java.util.stream.Collectors;

@Setter @Getter
public class AccessContext {
    public static final String ROLE_PREFIX = "ROLE_";
    private final Authentication authentication;
    private final RoleHierarchy roleHierarchy;
    private final AuthenticationTrustResolver trustResolver;
    private final Set<Authority> authoritySet;

    @Builder
    public AccessContext(Authentication authentication, RoleHierarchy roleHierarchy, AuthenticationTrustResolver trustResolver, Set<Authority> authoritySet) {
        this.authoritySet = authoritySet;
        this.authentication = authentication;
        this.roleHierarchy = roleHierarchy;
        this.trustResolver = trustResolver;
    }

    public final Set<RoleAuthority> getRoleAuthoritySet() {
        return authoritySet.stream()
                .filter(RoleAuthority.class::isInstance)
                .map(RoleAuthority.class::cast)
                .collect(Collectors.toSet());
    }

    public final Set<PermissionAuthority> getPermissionAuthoritySet() {
        return authoritySet.stream()
                .filter(PermissionAuthority.class::isInstance)
                .map(PermissionAuthority.class::cast)
                .collect(Collectors.toSet());
    }

    public String getUserId() {
        return authentication.getName();
    }

}
