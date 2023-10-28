package click.porito.modular_travel.account.internal.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalUtil {
    public static Long getAccountId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.valueOf(name);
    }
}
