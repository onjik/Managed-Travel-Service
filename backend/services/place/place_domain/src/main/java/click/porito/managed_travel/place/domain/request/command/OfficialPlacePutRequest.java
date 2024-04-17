package click.porito.managed_travel.place.domain.request.command;

import click.porito.managed_travel.place.domain.PlaceCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.geojson.Point;
import org.geojson.Polygon;

import java.util.List;

@Getter @Setter
public class OfficialPlacePutRequest extends AbstractPlaceCommandRequestBase{
    private Long placeId;
    private Boolean isPublic = true;
    private String googlePlaceId;

    @Builder
    public OfficialPlacePutRequest(String name, List<String> keywords, String address, String postalCode, String phoneNumber, String website, String summary, Point location, Polygon boundary, List<PlaceCategory> categories, Long placeId, String googlePlaceId, Boolean isPublic) {
        super(name, keywords, address, postalCode, phoneNumber, website, summary, location, boundary,categories);
        if(isPublic != null){
            this.isPublic = isPublic;
        }
        this.placeId = placeId;
        this.googlePlaceId = googlePlaceId;
    }
}
