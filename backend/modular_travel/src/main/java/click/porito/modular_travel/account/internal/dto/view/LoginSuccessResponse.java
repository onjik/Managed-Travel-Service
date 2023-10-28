package click.porito.modular_travel.account.internal.dto.view;

import click.porito.modular_travel.account.internal.config.OidcLoginSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import click.porito.modular_travel.account.internal.entity.Account;

import java.util.List;

/**
 * 로그인 성공시 응답에 사용되는 DTO
 * {@link OidcLoginSuccessHandler#onAuthenticationSuccess(HttpServletRequest, HttpServletResponse, Authentication)}}
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessResponse {
    private Long userId;
    private String name;
    private String profileImageUri;
    private List<Account.Role> roles;

    public static LoginSuccessResponse from(Account account) {
        return new LoginSuccessResponse(
                account.getUserId(),
                account.getName(),
                account.getProfileImgUri(),
                account.getRoles());
    }


}
