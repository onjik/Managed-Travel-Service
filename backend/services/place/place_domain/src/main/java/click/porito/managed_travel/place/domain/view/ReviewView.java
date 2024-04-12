package click.porito.managed_travel.place.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

import static click.porito.managed_travel.place.domain.PlaceDomainConstant.JACKSON_TIME_FORMAT;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewView {
    private Long reviewId;
    private Integer rating;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant updatedAt;
    private String placeId;
    private String publisherId;
    private List<PlaceMediaView> photos;
}