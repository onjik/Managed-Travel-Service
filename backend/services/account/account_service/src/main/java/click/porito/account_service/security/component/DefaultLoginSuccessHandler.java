package click.porito.account_service.security.component;

import click.porito.managed_travel.domain.domain.Account;
import click.porito.managed_travel.domain.event.AuthenticationSuccessEvent;
import click.porito.managed_travel.domain.event.SecurityTopics;
import click.porito.account_service.account.operation.AccountOperation;
import click.porito.account_service.security.exception.OidcUnexpectedServerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountOperation accountOperation;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();
        //response body
        responseLoginSuccess(response, userId);

        //kafka
        var event = createSuccessEvent(userId, request);
        kafkaTemplate.send(SecurityTopics.AUTHENTICATION_SUCCESS_0,event);
    }


    private void responseLoginSuccess(HttpServletResponse response, String userId) throws IOException {
        //response body
        Account account = accountOperation.findByUserId(userId)
                .orElseThrow(OidcUnexpectedServerError::new);
        LoginSuccessResponse responseBody = LoginSuccessResponse.from(account);
        response.setContentType("application/json");
        response.setStatus(200);
        String body = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(body);
    }

    public AuthenticationSuccessEvent createSuccessEvent(String userId, HttpServletRequest request) {
        return new AuthenticationSuccessEvent(userId, request.getRemoteAddr(), request.getRequestURI());
    }



    /**
     * 로그인 성공시 응답에 사용되는 DTO
     * {@link DefaultLoginSuccessHandler#onAuthenticationSuccess(HttpServletRequest, HttpServletResponse, Authentication)}}
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginSuccessResponse {
        private String userId;
        private String name;
        private String profileImageUri;
        private Collection<String> roles;

        public static LoginSuccessResponse from(Account account) {
            return new LoginSuccessResponse(
                    account.getUserId(),
                    account.getName(),
                    account.getProfileImgUri(),
                    account.getRoles());
        }


    }
}
