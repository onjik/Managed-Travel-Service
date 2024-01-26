package click.porito.place_common.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record Place(
    String id,
    String name,
    List<String> tags,
    String address,
    Double latitude,
    Double longitude,
    String summary,
    List<PhotoReference> photos
){
}
