package click.porito.account.security.domain;

import click.porito.account.global.constant.Domain;
import org.springframework.lang.Nullable;

public record PermissionAuthority(
        Domain domain,
        Action action,
        @Nullable
        Scope scope
) implements Authority {
}
