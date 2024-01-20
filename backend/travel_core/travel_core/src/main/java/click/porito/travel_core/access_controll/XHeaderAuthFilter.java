package click.porito.travel_core.access_controll;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class XHeaderAuthFilter extends OncePerRequestFilter {
    private final RequestMatcher requestMatcher;
    private final ObjectMapper objectMapper;
    private final static String X_USER_ID = "X-Authorization-Id";
    private final static String X_USER_ROLES = "X-Authorization-Roles";
    @Setter
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();


    public XHeaderAuthFilter(ObjectMapper objectMapper) {
        this.requestMatcher = new AndRequestMatcher(
                new RequestHeaderRequestMatcher(X_USER_ID),
                new RequestHeaderRequestMatcher(X_USER_ROLES)
        );
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Try to authenticate with X-Authorization header");
        // do authentication
        final XHeaderAuthentication authentication;
        try {
            authentication = parseAuthentication(request);
            onSuccessfulAuthentication(request, response, authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            onUnsuccessfulAuthentication(request, response, e);
            throw e;
        }
    }

    protected XHeaderAuthentication parseAuthentication(HttpServletRequest request) throws AuthenticationException{
        String userId = request.getHeader(X_USER_ID);
        String rolesHeader = request.getHeader(X_USER_ROLES);
        String[] roles = null;
        try {
            roles = objectMapper.readValue(rolesHeader, String[].class);//json 형식이 맞는지 확인
        } catch (Exception e) {
            log.debug("Error Occurred while parsing Roles header", e);
            throw new AuthenticationException("인증 정보 형식이 잘못되었습니다.",e) {};
        }
        return new XHeaderAuthentication(userId, roles);
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        // set authentication to security context
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.setContext(context);
        log.debug("Set Authentication to SecurityContextHolder. {}", authResult);

        log.debug("Authentication success. continue filter chain");
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.securityContextHolderStrategy.clearContext();
        log.debug("Authentication failed. clear SecurityContextHolder");
    }
}

