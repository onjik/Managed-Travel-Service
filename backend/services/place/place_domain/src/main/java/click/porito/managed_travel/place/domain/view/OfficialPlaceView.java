package click.porito.managed_travel.place.domain.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class OfficialPlaceView extends PlaceView {
    private String googlePlaceId;
}