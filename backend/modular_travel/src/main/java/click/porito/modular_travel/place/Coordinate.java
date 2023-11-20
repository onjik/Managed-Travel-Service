package click.porito.modular_travel.place;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private double latitude;
    private double longitude;

    public static Coordinate of(LatLng latLng) {
        return new Coordinate(latLng.lat, latLng.lng);
    }

}
