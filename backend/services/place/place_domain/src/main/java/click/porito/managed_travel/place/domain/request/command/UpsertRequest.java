package click.porito.managed_travel.place.domain.request.command;

public interface UpsertRequest {
    boolean isCreateRequest();
    boolean isUpdateRequest();
}
