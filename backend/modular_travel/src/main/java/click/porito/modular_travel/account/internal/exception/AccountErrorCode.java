package click.porito.modular_travel.account.internal.exception;

import org.springframework.lang.Nullable;
import click.porito.modular_travel.global.ErrorCode;

public enum AccountErrorCode implements ErrorCode {
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

    @Override
    public String getDomainName() {
        return DOMAIN_NAME;
    }

    @Override
    public String getErrorCode() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
