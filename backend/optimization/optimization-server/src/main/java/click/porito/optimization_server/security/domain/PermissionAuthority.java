package click.porito.optimization_server.security.domain;

import click.porito.optimization_server.global.constant.Domain;
import org.springframework.lang.Nullable;

public record PermissionAuthority(
        Domain domain,
        Action action,
        @Nullable
        Scope scope
) implements Authority{
}
