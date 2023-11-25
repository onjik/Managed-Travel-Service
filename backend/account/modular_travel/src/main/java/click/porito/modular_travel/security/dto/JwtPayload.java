package click.porito.modular_travel.security.dto;

import java.util.Collection;

public record JwtPayload (
        String userId,
        Collection<String> roles
) {
}
