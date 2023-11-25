package click.porito.modular_travel.security.filter;

import click.porito.modular_travel.security.constant.SecurityConstant;
import click.porito.modular_travel.security.model.SimpleAuthentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.BadCredentialsException;
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
import java.util.Arrays;

/**
 * ApiGateway 에서 넘겨준 Authorization Header 를 분석해서 SecurityContext 에 넣어주는 필터
 */
@Slf4j
@RequiredArgsConstructor
public class AuthorizationHeaderTranslationFilter extends OncePerRequestFilter {
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private RequestMatcher requestMatcher = new AndRequestMatcher(
            new RequestHeaderRequestMatcher(SecurityConstant.X_USER_ID),
            new RequestHeaderRequestMatcher(SecurityConstant.X_USER_ROLES)
    );

    private final ObjectMapper objectMapper;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String userId = request.getHeader(SecurityConstant.X_USER_ID);
        //list of roles
        String userRoles = request.getHeader(SecurityConstant.X_USER_ROLES);
        // 헤더 값이 유효하지 않으면, 인증을 진행하지 않는다.
        if (userId.isBlank() || userRoles.isBlank() || !isValidLong(userId)){
            return null;
        }
        String[] roleNames;
        try {
            roleNames = objectMapper.readValue(userRoles, String[].class);
        } catch (JsonProcessingException e) {
            //json parsing error
            log.error("failed to parse json, on header: {}", SecurityConstant.X_USER_ROLES, e);
            //bad request
            throw new BadCredentialsException("invalid header: " + SecurityConstant.X_USER_ROLES, e);
        }
        var authorities = Arrays.asList(roleNames);
        return new SimpleAuthentication(authorities, userId);
    }

    protected void successfulAuthentication(Authentication authResult) {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.setContext(context);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }
    }

    protected void unsuccessfulAuthentication(HttpServletResponse response, Exception failed) throws IOException {
        if (failed instanceof BadCredentialsException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } else if (failed instanceof AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, failed.getMessage());
        }

    }

    protected boolean isValidLong(String str){
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)){
            try {
                Authentication authentication = attemptAuthentication(request, response);
                if (authentication != null){
                    successfulAuthentication(authentication);
                }
            } catch (Exception e){
                unsuccessfulAuthentication(response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
        return;
    }
}
