package click.porito.place_service.external_api.google_api.model;

public final class FieldMasks {
    public static FieldMask[] DEFAULT_MASKS = {
            FieldMask.id,
            FieldMask.displayName,
            FieldMask.location,
            FieldMask.formattedAddress,
            FieldMask.editorialSummary,
            FieldMask.types,
            FieldMask.photos
    };
}
