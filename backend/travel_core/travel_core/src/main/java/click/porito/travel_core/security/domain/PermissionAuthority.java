package click.porito.travel_core.security.domain;

import click.porito.travel_core.global.constant.Domain;
import org.springframework.lang.Nullable;

public record PermissionAuthority(
        Domain domain,
        Action action,
        @Nullable
        Scope scope
) implements Authority{
}
