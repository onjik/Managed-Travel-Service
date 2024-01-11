package click.porito.travel_core.place.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PlaceView (
    String id,
    String name,
    List<String> tags,
    String address,
    Double latitude,
    Double longitude,
    String summary,
    List<PhotoReferenceView> photos
){
}
