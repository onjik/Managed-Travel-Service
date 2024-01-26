package click.porito.security.jwt_authentication.filter;

import click.porito.security.SecurityBusinessException;
import click.porito.security.SecurityServerException;
import click.porito.security.jwt_authentication.JwtAuthentication;
import click.porito.security.jwt_authentication.JwtOperation;
import click.porito.security.jwt_authentication.event.JwtAuthenticationFailedEvent;
import click.porito.security.jwt_authentication.event.JwtAuthenticationSuccessEvent;
import click.porito.security.jwt_authentication.exception.JwtNotExistException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter implements ApplicationEventPublisherAware {
    private final JwtOperation jwtOperation;
    @Setter
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private ApplicationEventPublisher eventPublisher;
    private final RequestMatcher requestMatcher = new RequestHeaderRequestMatcher(HttpHeaders.AUTHORIZATION);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String tokenValue = resolveToken(request).orElseThrow(JwtNotExistException::new);
        try {
            JwtAuthentication jwtAuthentication = jwtOperation.parseToken(tokenValue);
            onSuccessfulAuthentication(request, response, filterChain, jwtAuthentication);
        } catch (Exception e){
            if (logger.isErrorEnabled()){
                if (e instanceof SecurityBusinessException){
                    logger.error("An Business Side error occurred while trying to authenticate the user.", e);
                } else if (e instanceof SecurityServerException){
                    logger.error("An Server Side error occurred while trying to authenticate the user.", e);
                } else {
                    logger.error("An error occurred while trying to authenticate the user.", e);
                }
            }
            this.onUnsuccessfulAuthentication(request, response, e);
        }
    };

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.setContext(context);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }
        if (this.eventPublisher != null) {
            String userId = authResult.getName();
            this.eventPublisher.publishEvent(new JwtAuthenticationSuccessEvent(this,userId));
        }
        filterChain.doFilter(request, response);
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException, ServletException {
        this.securityContextHolderStrategy.clearContext();
        this.logger.trace("Failed to process authentication request", exception);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new JwtAuthenticationFailedEvent(this, exception));
        }
    }




    private Optional<String> resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
