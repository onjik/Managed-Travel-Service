package click.porito.travel_core.place.dao.google_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class LocationArea {

    @JsonProperty("circle")
    private Circle circle;

    public LocationArea(Circle circle) {
        this.circle = circle;
    }
}
