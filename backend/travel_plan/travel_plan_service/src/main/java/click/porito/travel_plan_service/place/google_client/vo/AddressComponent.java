package click.porito.travel_plan_service.place.google_client.vo;

public record AddressComponent(
        String longText,
        String shortText,
        String[] types,
        String languageCode
) {
}
