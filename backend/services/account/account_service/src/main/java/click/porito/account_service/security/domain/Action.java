package click.porito.account_service.security.domain;

public enum Action {
    CREATE,
    READ,
    UPDATE,
    DELETE,
    ;

    public static Action of(String action) {
        return Action.valueOf(action.toUpperCase());
    }
}
