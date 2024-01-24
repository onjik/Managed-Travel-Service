package click.porito.account.account.operation.adapter.persistence.entity;

public enum GenderProperty {
    MALE, FEMALE;

    @Override
    public String toString() {
        return this.name();
    }
}
