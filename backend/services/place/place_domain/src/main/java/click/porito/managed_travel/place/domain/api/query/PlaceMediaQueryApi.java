package click.porito.managed_travel.place.domain.api.query;

import click.porito.common.util.PageableRequest;
import click.porito.common.util.SlicedResponse;
import click.porito.managed_travel.place.domain.view.PlaceMediaView;

public interface PlaceMediaQueryApi {
    PlaceMediaView getPlaceMediaById(Long mediaId);
    SlicedResponse<PlaceMediaView> getPlaceMediaByPlaceId(Long placeId, PageableRequest request);
    SlicedResponse<PlaceMediaView> getPlaceMediaByOwnerId(String ownerId, PageableRequest request);
}
