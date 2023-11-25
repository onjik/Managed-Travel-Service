package click.porito.modular_travel.security.component;

import click.porito.modular_travel.account.AccountDTO;
import click.porito.modular_travel.account.AccountService;
import click.porito.modular_travel.security.event.AuthenticationMethod;
import click.porito.modular_travel.security.event.AuthenticationSuccessEvent;
import click.porito.modular_travel.security.event.SecurityTopics;
import click.porito.modular_travel.security.exception.OidcUnexpectedServerError;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();
        AuthenticationSuccessEvent successEvent = new AuthenticationSuccessEvent(userId, AuthenticationMethod.OIDC);
        kafkaTemplate.send(SecurityTopics.AUTHENTICATION_SUCCESS_0,successEvent);


        //response body
        responseLoginSuccess(response, userId);
    }


    private void responseLoginSuccess(HttpServletResponse response, String userId) throws IOException {
        //response body
        Long userIdLong = Long.parseLong(userId);
        AccountDTO account = accountService.retrieveAccountById(userIdLong)
                .orElseThrow(OidcUnexpectedServerError::new);
        LoginSuccessResponse responseBody = LoginSuccessResponse.from(account);
        response.setContentType("application/json");
        response.setStatus(200);
        String body = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(body);
    }


    /**
     * 로그인 성공시 응답에 사용되는 DTO
     * {@link DefaultLoginSuccessHandler#onAuthenticationSuccess(HttpServletRequest, HttpServletResponse, Authentication)}}
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginSuccessResponse {
        private Long userId;
        private String name;
        private String profileImageUri;
        private Collection<String> roles;

        public static LoginSuccessResponse from(AccountDTO account) {
            return new LoginSuccessResponse(
                    account.getUserId(),
                    account.getName(),
                    account.getProfileImgUri(),
                    account.getRoleNames());
        }


    }
}
