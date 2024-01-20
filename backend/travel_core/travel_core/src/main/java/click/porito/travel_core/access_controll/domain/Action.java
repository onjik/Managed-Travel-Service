package click.porito.travel_core.access_controll.domain;

/**
 * Action을 나타낸다. 무조건 대문자로 적어야 한다.
 * @see click.porito.travel_core.access_controll.domain.AuthorityMapper#of(String)
 */
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
