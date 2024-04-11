package click.porito.managed_travel.place.domain.api.command;

import click.porito.managed_travel.place.domain.request.command.MediaUploadForm;
import click.porito.managed_travel.place.domain.request.command.PlaceMediaUploadRequest;
import click.porito.managed_travel.place.domain.view.PlaceMediaView;

public interface PlaceMediaCommandApi {
    PlaceMediaView[] putPlaceMedia(PlaceMediaUploadRequest request);
    void deletePlaceMedia(Long mediaId);
}
