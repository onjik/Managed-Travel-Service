package click.porito.modular_travel.security.component;

import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.security.constant.SecurityConstant;
import click.porito.modular_travel.security.exception.InsufficientRegisterInfoException;
import click.porito.modular_travel.security.exception.OidcEmailNotVerifiedException;
import click.porito.modular_travel.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLoginFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof InvalidCookieException){
            responseError(response, "{\"message\":\"invalid cookie\"}", HttpServletResponse.SC_BAD_REQUEST);
        } else if (exception instanceof OidcEmailNotVerifiedException e){
            Map<String, String> body = Map.of(
                    "message", "email not verified",
                    "email", e.getEmail(),
                    "issuer", e.getIssuer()
            );
            responseError(response, body, HttpServletResponse.SC_UNAUTHORIZED);
        } else if (exception instanceof InsufficientRegisterInfoException e){
            //영속화
            AccountRegisterDTO registerForm = e.getRegisterForm();
            String token = jwtService.encodeRegisterDTO(registerForm);
            //cookie
            Cookie cookie = new Cookie(SecurityConstant.INSUFFICIENT_FORM_TOKEN, token);
            cookie.setPath(SecurityConstant.SUPPLEMENT_REGISTER_URI);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 60); //30분
            response.addCookie(cookie);

            //response body
            List<String> invalidFields = e.getViolations().stream()
                    .map(ConstraintViolation::getPropertyPath)
                    .map(Object::toString)
                    .toList();
            Map<String,Object> body = Map.of(
                    "message", "insufficient user info",
                    "invalidFields", invalidFields
            );
            responseError(response, body, HttpServletResponse.SC_PRECONDITION_FAILED);
        } else if (exception instanceof AuthenticationCredentialsNotFoundException e){
            responseError(response, "{\"message\":\"bad request\"}", HttpServletResponse.SC_BAD_REQUEST);
        } else if (exception instanceof InternalAuthenticationServiceException e) {
            log.error("Server Error Occurred While Authentication",e);
            responseError(response, "{\"message\":\"internal server error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            log.error("unexpected authentication exception", exception);
            responseError(response, "{\"message\":\"unexpected error\"}", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void responseError(HttpServletResponse response, Object body, int status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        if (body instanceof String){
            writer.write((String) body);
        } else {
            String json = objectMapper.writeValueAsString(body);
            writer.write(json);
        }
        return;
    }


}
