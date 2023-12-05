package click.porito.travel_plan_service.place.google_client;

import click.porito.travel_plan_service.place.dto.BusinessStatus;
import click.porito.travel_plan_service.place.dto.Coordinate;
import click.porito.travel_plan_service.place.dto.PlacePhotoReference;
import click.porito.travel_plan_service.place.google_client.vo.AddressComponent;
import click.porito.travel_plan_service.place.google_client.vo.LocalizedString;

public record GooglePlaceDetailResponseMapper(
        String id,
        LocalizedString displayName,
        LocalizedString editorialSummary,
        String formattedAddress,
        Coordinate location,
        Double rating,
        Integer userRatingCount,
        AddressComponent[] addressComponents,
        BusinessStatus businessStatus,
        String[] types,
        PlacePhotoReference[] photos
){
}
