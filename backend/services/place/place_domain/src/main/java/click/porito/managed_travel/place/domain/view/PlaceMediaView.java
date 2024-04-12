package click.porito.managed_travel.place.domain.view;

import click.porito.managed_travel.place.domain.PlaceDomainConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static click.porito.managed_travel.place.domain.PlaceDomainConstant.*;

@Data
public class PlaceMediaView {
    private Long mediaId;
    private Integer widthPx;
    private Integer heightPx;
    private Map<String, String> sourceReference = new HashMap<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant createdAt;
    private String contentType;
    private String placeId;
    private String ownerId;
    private String url;
}