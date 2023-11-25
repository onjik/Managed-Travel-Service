package click.porito.modular_travel.account;

public enum Gender {
    MALE, FEMALE;

    @Override
    public String toString() {
        return this.name();
    }
}
