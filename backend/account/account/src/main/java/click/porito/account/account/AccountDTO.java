package click.porito.account.account;

import click.porito.account.account.model.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * {@link Account} 의 DTO
 */
public interface AccountDTO extends AccountSummaryDTO {

    String getEmail();

    Collection<String> getPrefixedRoleNames();
    Instant getCreatedAt();
    Gender getGender();

    LocalDate getBirthDate();

    default OidcUser toOidcUser(final OidcIdToken idToken){
        Assert.notEmpty(idToken.getClaims(), "claims must not be empty");

        //idToken 의 sub(Subject) 을 account 의 userId 로 변경 하여 다시 생성
        HashMap<String, Object> map = new HashMap<>(idToken.getClaims());
        map.put(IdTokenClaimNames.SUB, this.getUserId());
        OidcIdToken token = new OidcIdToken(
                idToken.getTokenValue(),
                idToken.getIssuedAt(),
                idToken.getExpiresAt(),
                Collections.unmodifiableMap(map)
        );
        List<SimpleGrantedAuthority> authorities = this.getPrefixedRoleNames().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new DefaultOidcUser(authorities, token);
    }


    @Getter
    @Builder
    @RequiredArgsConstructor
    static class AccountDtoImpl implements AccountDTO{
        private final Long userId;
        private final String name;
        private final String profileImgUri;
        private final String email;
        private final Collection<String> roleNames;
        private final Instant createdAt;
        private final Gender gender;
        private final LocalDate birthDate;


        @Override
        public Collection<String> getPrefixedRoleNames() {
            return roleNames;
        }
    }

}
