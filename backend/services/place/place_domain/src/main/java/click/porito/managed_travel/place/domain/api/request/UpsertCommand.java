package click.porito.managed_travel.place.domain.api.request;

public interface UpsertCommand {
    boolean isUpdateCommand();
    boolean isCreateCommand();
}
