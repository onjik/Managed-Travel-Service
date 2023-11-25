package click.porito.gateway.service;

import java.util.Collection;

public record JwtPayLoad(
        String userId,
        Collection<String> roles
) {
}

