package click.porito.modular_travel.security.component;

import click.porito.modular_travel.security.exception.OidcEmailNotVerifiedException;
import click.porito.modular_travel.security.exception.OidcInsufficientUserInfoException;
import click.porito.modular_travel.account.AccountExternalApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class OidcLoginFailureHandler implements AuthenticationFailureHandler {
    public static final String INSUFFICIENT_EXCEPTION_KEY = OidcInsufficientUserInfoException.class.getName();
    private final ObjectMapper objectMapper;

    public OidcLoginFailureHandler(AccountExternalApi accountExternalApi, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof OidcEmailNotVerifiedException e){
            Map<String, String> body = Map.of(
                    "message", "email not verified",
                    "email", e.getEmail(),
                    "issuer", e.getIssuer()
            );
            String jsonBody = objectMapper.writeValueAsString(body);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, jsonBody);

        } else if (exception instanceof OidcInsufficientUserInfoException e){
            //처음 로그인 하는 유저가 타는 플로우
            /*
            1. 일단 저 OAuth2User 를 세션에 저장한다.
            2. 부족한 정보를 응답에 json으로 담아서 응답한다.
            3. 클라이언트는 이에 맞는 정보를 추가 입력하여 API를 호출한다.
            4. 해당 API 는 모든 정보를 입력하였는지 체크 후, 유저를 등록하고, SecurityContextHolder 에 Authentication 을 저장한다.
             */
            request.getSession().setAttribute(INSUFFICIENT_EXCEPTION_KEY, e);
            List<String> invalidFields = e.getViolations().stream()
                    .map(ConstraintViolation::getPropertyPath)
                    .map(Object::toString)
                    .toList();
            Map<String,Object> body = Map.of(
                    "message", "insufficient user info",
                    "invalidFields", invalidFields
            );
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            objectMapper.writeValue(response.getWriter(), body);
            //TODO 로그인 성공시, 테스트 코드, 보충 API, API 명세 업데이트, 이미지 서비스 수정
        } else if (exception instanceof AuthenticationCredentialsNotFoundException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"message\":\"invalid request\"}");
        } else if (exception instanceof InternalAuthenticationServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"message\":\"internal server error\"}");
        } else {
            //그 외의 인증 예외시
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\":\"authentication fail\"}");
        }
    }

}
