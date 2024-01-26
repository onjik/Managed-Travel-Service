package click.porito.account_common.event;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;

public record AuthenticationFailEvent(
        String requestIp,
        String requestUrl,
        String exceptionName,
        String message
) {
    public static AuthenticationFailEvent from(HttpServletRequest request, AuthenticationException exception) {
        return new AuthenticationFailEvent(
                request.getRemoteAddr(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
    }
}
