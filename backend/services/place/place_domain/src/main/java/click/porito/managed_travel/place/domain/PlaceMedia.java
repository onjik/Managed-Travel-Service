package click.porito.managed_travel.place.domain;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class PlaceMedia {
    private String mediaId;
    private Integer widthPx;
    private Integer heightPx;
    private Map<String,String> sourceRef;
    private Boolean isGooglePhoto;
    private Instant createdAt;
    private String contentType;
    private String placeId;
    private String ownerId;
}