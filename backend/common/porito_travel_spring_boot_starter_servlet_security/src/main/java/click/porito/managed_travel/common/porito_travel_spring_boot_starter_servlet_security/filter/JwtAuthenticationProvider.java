package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.filter;

import click.porito.common.security.UserContext;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.jwt_authentication.JwtOperation;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtOperation jwtOperation;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!this.supports(authentication.getClass())) {
            return null;
        }
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String tokenValue = jwtAuthenticationToken.token();
        if (tokenValue == null) {
            return null;
        }
        try {
            UserContext userContext = jwtOperation.parseToken(tokenValue);
            return new JwtAuthentication(userContext);
        } catch (Exception e) {
            throw new BadCredentialsException("Failed to authenticate token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
