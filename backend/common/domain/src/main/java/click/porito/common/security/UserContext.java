package click.porito.common.security;

import java.util.Collection;
import java.util.List;

public record UserContext(
        String userId,
        List<String> roles
) {
}
