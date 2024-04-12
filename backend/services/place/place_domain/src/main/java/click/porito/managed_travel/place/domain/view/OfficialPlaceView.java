package click.porito.managed_travel.place.domain.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class OfficialPlaceView extends PlaceView {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String googlePlaceId;
}