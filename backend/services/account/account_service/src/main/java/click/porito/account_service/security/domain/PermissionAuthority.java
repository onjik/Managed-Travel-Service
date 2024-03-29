package click.porito.account_service.security.domain;

import click.porito.common.exception.Domain;
import org.springframework.lang.Nullable;

public record PermissionAuthority(
        Domain domain,
        Action action,
        @Nullable
        Scope scope
) implements Authority {
}
