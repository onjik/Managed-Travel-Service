package click.porito.managed_travel.place.place_service.google_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public final class Circle {
    @JsonProperty("center")
    private Coordinate center;
    @JsonProperty("radius")
    private double radius;

    public static Circle of(double latitude, double longitude, double radius) {
        return new Circle(new Coordinate(latitude, longitude), radius);
    }

    public Circle(Coordinate center, double radius) {
        this.center = center;
        this.radius = radius;
    }
}
