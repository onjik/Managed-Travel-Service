package click.porito.managed_travel.place.domain.view;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class PlaceMediaView {
    private Long mediaId;
    private Integer widthPx;
    private Integer heightPx;
    private Map<String,String> sourceRef;
    private Instant createdAt;
    private String contentType;
    private String placeId;
    private String ownerId;
    private String url;
}