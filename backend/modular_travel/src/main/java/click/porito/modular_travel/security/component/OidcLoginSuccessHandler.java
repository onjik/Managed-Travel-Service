package click.porito.modular_travel.security.component;

import click.porito.modular_travel.account.AccountDTO;
import click.porito.modular_travel.account.AccountInternalApi;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class OidcLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountInternalApi accountInternalApi;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OidcUser oidcUser) {
            //이미 등록되었던 유저이고, 로그인에 성공했을 때 흐름입니다.
            String subject = oidcUser.getSubject();
            Long userId = Long.parseLong(subject);
            AccountDTO account = accountInternalApi.getAccountByUserId(userId)
                    .orElseThrow(OidcUnexpectedServerError::new);
            responseLoginSuccess(response, account);
        } else {
            //그 외의 인증 결과 - 아직 지원하지 않음
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"message\":\"unknown authentication\"}");
        }
    }


    private void responseLoginSuccess(HttpServletResponse response, AccountDTO account) throws IOException {
        LoginSuccessResponse responseBody = LoginSuccessResponse.from(account);
        response.setContentType("application/json");
        response.setStatus(200);
        objectMapper.writeValue(response.getWriter(), responseBody);
    }


    /**
     * 로그인 성공시 응답에 사용되는 DTO
     * {@link OidcLoginSuccessHandler#onAuthenticationSuccess(HttpServletRequest, HttpServletResponse, Authentication)}}
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
