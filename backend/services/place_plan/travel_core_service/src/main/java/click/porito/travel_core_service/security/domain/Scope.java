package click.porito.travel_core_service.security.domain;

/**
 * 접근하려는 대상을 나타낸다. 무조건 대문자로 적어야 한다.
 * @see click.porito.travel_core_service.security.domain.AuthorityMapper#of(String)
 */
public enum Scope {
    OWNED,
    BELONGED,
    ALL,
    NEW//only for create
}
