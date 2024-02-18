package click.porito.managed_travel.domain.domain;

public enum Gender {
    MALE, FEMALE;

    @Override
    public String toString() {
        return this.name();
    }
}
