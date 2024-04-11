package click.porito.managed_travel.place.domain.request.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ReviewCreateRequest {
    @NotBlank
    private String placeId;
    private String content;
    @NotNull
    @Range(min = 1, max = 10)
    private Integer rating;
    @Length(max = 20)
    private List<String> mediaIds;
    public ReviewCreateRequest(String placeId, String content, int rating, List<String> mediaIds) {
        this.placeId = placeId;
        this.content = content;
        this.rating = rating;
        this.mediaIds = mediaIds;
    }

}
