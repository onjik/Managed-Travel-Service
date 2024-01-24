package click.porito.account.security.domain;

/**
 * @param role unprefixed role name (e.g. ADMIN, USER)
 */
public record RoleAuthority(String role) implements Authority {
}
