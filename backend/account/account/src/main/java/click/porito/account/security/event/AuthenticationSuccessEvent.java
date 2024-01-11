package click.porito.account.security.event;

import jakarta.servlet.http.HttpServletRequest;

public record AuthenticationSuccessEvent (
        String userId,
        String requestIp,
        String requestUrl
){
    public static AuthenticationSuccessEvent from(String userId, HttpServletRequest request) {
        return new AuthenticationSuccessEvent(userId, request.getRemoteAddr(), request.getRequestURI());
    }
}
