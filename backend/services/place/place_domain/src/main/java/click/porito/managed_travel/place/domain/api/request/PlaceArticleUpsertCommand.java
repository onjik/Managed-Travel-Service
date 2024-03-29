package click.porito.managed_travel.place.domain.api.request;

import lombok.Data;

@Data
public class PlaceArticleUpsertCommand implements UpsertCommand{
    private String placeId;
    private String title;
    private String content;
    private boolean isPublished;
    private boolean isTemp;

    @Override
    public boolean isUpdateCommand() {
        return placeId != null;
    }

    @Override
    public boolean isCreateCommand() {
        return placeId == null;
    }
}
