package click.porito.account.security.domain;

import java.util.Collection;

public record JwtPayload (
        String userId,
        Collection<String> roles
) {
}
