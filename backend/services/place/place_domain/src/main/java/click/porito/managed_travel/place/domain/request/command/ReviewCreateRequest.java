package click.porito.managed_travel.place.domain.request.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter @Setter
@NoArgsConstructor
public class ReviewCreateRequest {
    @NotBlank
    private String placeId;
    private String content;
    @NotNull
    @Range(min = 1, max = 10)
    private Integer rating;
    public ReviewCreateRequest(String placeId, String content, int rating) {
        this.placeId = placeId;
        this.content = content;
        this.rating = rating;
    }

}
