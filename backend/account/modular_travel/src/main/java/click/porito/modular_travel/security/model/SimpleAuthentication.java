package click.porito.modular_travel.security.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

public class SimpleAuthentication extends AbstractAuthenticationToken {
    private final HeaderAuthenticationDetails details;


    public SimpleAuthentication(Collection<String> authorities, String userId) {
        super(authorities.stream()
                .map(roleName -> (GrantedAuthority) () -> roleName)
                .toList());
        super.setAuthenticated(true);
        this.details = new HeaderAuthenticationDetails(userId);
        super.setDetails(details);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return details;
    }

    @RequiredArgsConstructor
    static class HeaderAuthenticationDetails implements Principal {
        private final String userId;

        @Override
        public String getName() {
            return this.userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HeaderAuthenticationDetails that = (HeaderAuthenticationDetails) o;
            return Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId);
        }
    }
}
