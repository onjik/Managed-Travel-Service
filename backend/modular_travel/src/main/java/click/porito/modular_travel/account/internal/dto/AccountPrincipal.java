package click.porito.modular_travel.account.internal.dto;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.IS_NEW_ACCOUNT;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.USER_ID;

/**
 * 발급된 OidcUser 를 편리하게 사용하기 위해 메서드를 추가한 클래스
 */
public class AccountPrincipal extends DefaultOidcUser {
    public AccountPrincipal(OidcUser oidcUser) {
        super(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo());
        boolean userIdExist = oidcUser.getAttribute(USER_ID) instanceof Long;
        boolean isNewAccountFlagExist = oidcUser.getAttribute(IS_NEW_ACCOUNT) instanceof Boolean;
        if (!userIdExist || !isNewAccountFlagExist) {
            throw new IllegalArgumentException("OidcUser must have userId and isNewAccount attribute");
        }
    }

    public static AccountPrincipal from(OidcUser oidcUser) {
        return new AccountPrincipal(oidcUser);
    }

    public long getUserId() {
        return (long) super.getAttribute(USER_ID);
    }

    public boolean isNewAccount() {
        return (boolean) super.getAttribute(IS_NEW_ACCOUNT);
    }

    public static interface AttributeName {
        public static final String IS_NEW_ACCOUNT = "x-isNewAccount";
        public static final String USER_ID = "x-userId";
    }
}
