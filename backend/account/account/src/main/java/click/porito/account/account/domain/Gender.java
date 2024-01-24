package click.porito.account.account.domain;

public enum Gender {
    MALE, FEMALE;

    @Override
    public String toString() {
        return this.name();
    }
}
