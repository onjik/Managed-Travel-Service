package click.porito.optimization_server.security.domain;

/**
 * @param role unprefixed role name (e.g. ADMIN, USER)
 */
public record RoleAuthority(String role) implements Authority {
}
