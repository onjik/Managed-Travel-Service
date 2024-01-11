package click.porito.account.account.exception;

import org.springframework.lang.Nullable;

public enum AccountErrorCode {
    UNEXPECTED_OIDC_AUTHENTICATION_EXCEPTION("UNEXPECTED_OIDC_AUTHENTICATION_EXCEPTION", "unexpected Oidc Authentication Exception");

    private static final String DOMAIN_NAME = "ACCOUNT";

    private String errorCode;
    private String description;

    AccountErrorCode(String errorCode, @Nullable String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    AccountErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDomainName() {
        return DOMAIN_NAME;
    }

}
