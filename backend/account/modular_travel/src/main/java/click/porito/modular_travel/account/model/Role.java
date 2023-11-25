package click.porito.modular_travel.account.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER;
    private String authority;

    Role() {
        this.authority = "ROLE_" + this.name();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
