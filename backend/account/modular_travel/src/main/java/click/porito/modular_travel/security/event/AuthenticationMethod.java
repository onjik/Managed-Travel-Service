package click.porito.modular_travel.security.event;

public enum AuthenticationMethod {
    OIDC, INFO_SUPPLY;

    @Override
    public String toString() {
        return this.name();
    }
}
