package click.porito.managed_travel.place.domain.request.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class PlaceArticleCreateRequest {
    @NotBlank
    @Length(max = 180)
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String placeId;
    @NotNull
    private Boolean isTemp;

}
