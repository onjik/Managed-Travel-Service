package click.porito.account.account;

import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import click.porito.account.gateway.router.AccountRouter;

import java.time.LocalDate;

/**
 * 계정 수정을 위한 요청에 사용되는 DTO
 * {@link AccountRouter#patchProfileInfo(OidcUser, AccountPatchDTO)}
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountPatchDTO {
    @Length(min = 2, max = 20)
    private String name;

    private Gender gender;

    @Past
    private LocalDate birthDate;

}
