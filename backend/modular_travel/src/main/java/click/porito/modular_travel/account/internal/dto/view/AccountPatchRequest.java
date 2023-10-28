package click.porito.modular_travel.account.internal.dto.view;

import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import click.porito.modular_travel.account.internal.controller.AccountController;
import click.porito.modular_travel.account.internal.entity.Account;

import java.time.LocalDate;

/**
 * 계정 수정을 위한 요청에 사용되는 DTO
 * {@link AccountController#patchProfileInfo(OidcUser, AccountPatchRequest)}
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountPatchRequest {
    @Length(min = 2, max = 20)
    private String name;

    private Account.Gender gender;

    @Past
    private LocalDate birthDate;

}
