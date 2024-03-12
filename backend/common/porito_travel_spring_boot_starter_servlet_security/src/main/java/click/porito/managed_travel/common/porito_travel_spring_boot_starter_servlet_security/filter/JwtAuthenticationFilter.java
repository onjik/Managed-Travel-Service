package click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_security.filter;

import click.porito.managed_travel.common.porito_travel_spring_boot_starter_jwt.exception.JwtNotExistException;
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
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthenticationManager authenticationManager;
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
        JwtAuthenticationToken token = new JwtAuthenticationToken(tokenValue);
        try {
            Authentication authentication = this.authenticationManager.authenticate(token);
            onSuccessfulAuthentication(request, response, filterChain, authentication);
        } catch (Exception e){
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
        filterChain.doFilter(request, response);
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException, ServletException {
        this.securityContextHolderStrategy.clearContext();
        this.logger.trace("Failed to process authentication request", exception);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
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
