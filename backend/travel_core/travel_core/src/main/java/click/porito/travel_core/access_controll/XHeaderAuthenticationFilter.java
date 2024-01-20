package click.porito.travel_core.access_controll;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import java.io.IOException;

/**
 * X-Authorization-Id 헤더와 X-Authorization-Roles 헤더가 존재하면, 해당 정보를 Authentication 에 담아
 * SecurityContextHolder 에 저장하는 필터
 */
public class XHeaderAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static String X_USER_ID = "X-Authorization-Id";
    private final static String X_USER_ROLES = "X-Authorization-Roles";
    private final ObjectMapper objectMapper;

    public XHeaderAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AndRequestMatcher(
                new RequestHeaderRequestMatcher(X_USER_ID),
                new RequestHeaderRequestMatcher(X_USER_ROLES)
        ));
        super.setAllowSessionCreation(false);
        super.setAuthenticationSuccessHandler(new NullSuccessHandler());
        this.objectMapper = objectMapper;
    }

    /**
     * X-Authorization-Id 헤더와 X-Authorization-Roles 헤더를 이용하여 인증을 시도한다.
     * @throws AuthenticationException 인증 정보 형식이 잘못되었을 경우
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String userId = request.getHeader(X_USER_ID);
        String[] roles = null;
        try {
            String rolesHeader = request.getHeader(X_USER_ROLES);
            roles = objectMapper.readValue(rolesHeader, String[].class);//json 형식이 맞는지 확인
        } catch (Exception e) {
            //AuthenticationException : 인증 정보 형식이 잘못되었음
            throw new AuthenticationException("인증 정보 형식이 잘못되었습니다.",e) {};
        }
        return new XHeaderAuthentication(userId, roles);
        //TODO 필요한 경우 AuthenticationManager 와 authenticated 처리를 하도록 확장 가능 (여러 인증 수단 추가시)
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        //필터 체인을 계속 진행한다.
        chain.doFilter(request, response);
    }

    static class NullSuccessHandler implements AuthenticationSuccessHandler {
         @Override
         public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
             //아무 작업도 하지 않는다.
         }
     }
}
