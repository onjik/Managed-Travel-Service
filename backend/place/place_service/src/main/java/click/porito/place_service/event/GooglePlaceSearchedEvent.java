package click.porito.place_service.event;

public record GooglePlaceSearchedEvent(
    String placeId,
    String userId,
    Double latitude,
    Double longitude
) {

}
