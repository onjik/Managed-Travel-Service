package click.porito.managed_travel.place.domain.api.request;

import lombok.Data;

import java.util.Map;

@Data
public class PlaceMediaCreateRequest {
    private byte[] media;
    private String contentType;
    private String placeId;
    private Map<String,String> sourceRef;
}
