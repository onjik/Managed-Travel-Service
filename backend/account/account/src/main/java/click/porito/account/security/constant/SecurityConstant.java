package click.porito.account.security.constant;

public interface SecurityConstant {
    String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";
    String INSUFFICIENT_FORM_TOKEN = "insufficient-form-token";
    String X_USER_ID = "X-Authorization-Id";
    String X_USER_ROLES = "X-Authorization-Roles";
    String SUPPLEMENT_REGISTER_URI = "/account/register";
    String REFRESH_URI = "/account/refresh";
}
