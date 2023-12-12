package click.porito.place_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Circle {
    @JsonProperty("center")
    private Coordinate center;
    @JsonProperty("radius")
    private double radiusMeters;

    public static Circle of(double latitude, double longitude, double radius) {
        return new Circle(new Coordinate(latitude, longitude), radius);
    }


}
