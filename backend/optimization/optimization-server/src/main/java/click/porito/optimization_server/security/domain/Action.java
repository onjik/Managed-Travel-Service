package click.porito.optimization_server.security.domain;


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
