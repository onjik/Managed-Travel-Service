package click.porito.managed_travel.place.domain.request.command;

import java.time.Instant;
import java.util.Map;

public class PlaceMediaUploadRequest {
    private String placeId;
    private MediaUploadForm[] mediaUploadForms;
    private String contentType;
    private Map<String,String> sourceRef;
}
