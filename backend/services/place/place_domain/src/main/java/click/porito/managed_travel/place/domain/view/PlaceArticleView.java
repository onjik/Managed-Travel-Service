package click.porito.managed_travel.place.domain.view;

import click.porito.managed_travel.place.domain.PlaceDomainConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

import static click.porito.managed_travel.place.domain.PlaceDomainConstant.*;

@Data
public class PlaceArticleView {
    private String articleId;
    private String title;
    private String content;
    private String publisherId;
    private String placeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JACKSON_TIME_FORMAT)
    private Instant updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isTemp;
}