package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security;

import click.porito.common.security.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record JwtAuthentication(
        String userId,
        Collection<String> roles
) implements Authentication {

    public JwtAuthentication(UserContext userContext) {
        this(userContext.userId(), userContext.roles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return userId;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // do nothing
    }

    @Override
    public String getName() {
        return userId;
    }
}
