package click.porito.managed_travel.place.domain.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class ReviewUpsertCommand implements UpsertCommand{
    private Long reviewId;
    @Range(min = 1, max = 10)
    private Integer rating;
    private String content;
    @NotBlank
    private String placeId;
    private List<String> mediaIds;

    @Override
    public boolean isUpdateCommand() {
        return reviewId != null;
    }

    @Override
    public boolean isCreateCommand() {
        return reviewId == null;
    }
}
