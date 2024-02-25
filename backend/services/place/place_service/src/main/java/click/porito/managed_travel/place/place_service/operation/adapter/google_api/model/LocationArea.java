package click.porito.managed_travel.place.place_service.operation.adapter.google_api.model;

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
