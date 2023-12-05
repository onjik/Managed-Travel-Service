package click.porito.travel_plan_service.place.google_client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FieldMask {
    id("id", "places.id"),
    businessStatus("businessStatus", "places.businessStatus"),
    displayName("displayName", "places.displayName"),
    location("location", "places.location"),
    formattedAddress("formattedAddress", "places.formattedAddress"),
    types("types", "places.types"),
    utcOffsetMinutes("utcOffsetMinutes", "places.utcOffsetMinutes"),
    photos("photos", "places.photos"),
    primaryType("primaryType", "places.primaryType"),
    primaryTypeDisplayName("primaryTypeDisplayName", "places.primaryTypeDisplayName"),
    shortFormattedAddress("shortFormattedAddress", "places.shortFormattedAddress"),
    iconBackgroundUri("iconBackgroundUri", "places.iconBackgroundUri"),
    iconMaskBaseUri("iconMaskBaseUri", "places.iconMaskBaseUri"),
    googleMapsUri("googleMapsUri", "places.googleMapsUri"),
    editorialSummary("editorialSummary", "places.editorialSummary"),
    rating("rating", "places.rating"),
    addressComponents("addressComponents", "places.addressComponents"),
    userRatingCount("userRatingCount", "places.userRatingCount"),
    priceLevel("priceLevel", "places.priceLevel");
    private final String maskName;
    private final String prefixedMaskName;
}
