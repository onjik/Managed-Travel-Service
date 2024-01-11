package click.porito.account.security.dto;

import java.util.Collection;

public record JwtPayload (
        String userId,
        Collection<String> roles
) {
}
