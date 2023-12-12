package click.porito.place_service.dto;

import click.porito.place_service.google_api.ApiPrice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FieldMask {
    id("id", "places.id", ApiPrice.ID_ONLY),
    businessStatus("businessStatus", "places.businessStatus", ApiPrice.BASIC),
    displayName("displayName", "places.displayName", ApiPrice.BASIC),
    location("location", "places.location", ApiPrice.BASIC),
    formattedAddress("formattedAddress", "places.formattedAddress", ApiPrice.BASIC),
    types("types", "places.types", ApiPrice.BASIC),
    utcOffsetMinutes("utcOffsetMinutes", "places.utcOffsetMinutes", ApiPrice.BASIC),
    photos("photos", "places.photos", ApiPrice.ID_ONLY),
    primaryType("primaryType", "places.primaryType", ApiPrice.BASIC),
    primaryTypeDisplayName("primaryTypeDisplayName", "places.primaryTypeDisplayName", ApiPrice.BASIC),
    shortFormattedAddress("shortFormattedAddress", "places.shortFormattedAddress", ApiPrice.BASIC),
    iconBackgroundUri("iconBackgroundUri", "places.iconBackgroundUri", ApiPrice.BASIC),
    iconMaskBaseUri("iconMaskBaseUri", "places.iconMaskBaseUri", ApiPrice.BASIC),
    googleMapsUri("googleMapsUri", "places.googleMapsUri", ApiPrice.BASIC);
    private final String maskName;
    private final String prefixedMaskName;
    private final ApiPrice priceLevel;
}
