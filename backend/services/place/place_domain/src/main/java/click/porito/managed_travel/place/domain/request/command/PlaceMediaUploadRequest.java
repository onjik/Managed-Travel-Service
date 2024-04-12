package click.porito.managed_travel.place.domain.request.command;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter @Setter
public class PlaceMediaUploadRequest {
    private String placeId;
    private MediaUploadForm[] mediaUploadForms;
    private String contentType;
    private Map<String,String> sourceRef;
}
