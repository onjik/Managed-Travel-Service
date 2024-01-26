package click.porito.account_common.domain;

public enum Gender {
    MALE, FEMALE;

    @Override
    public String toString() {
        return this.name();
    }
}
