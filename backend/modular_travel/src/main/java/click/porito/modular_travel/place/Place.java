package click.porito.modular_travel.place;

import click.porito.modular_travel.image.Photo;

public interface Place {
    String getId();
    BusinessStatus getBusinessStatus();
    LocalizedString getDisplayName();
    Coordinate getLocation();
    String getFormattedAddress();
    String[] getTypes();
    Integer getUtcOffsetMinutes();
    Photo[] getPhotos();

    enum BusinessStatus {
        BUSINESS_STATUS_UNSPECIFIED,
        OPERATIONAL,
        CLOSED_TEMPORARILY,
        CLOSED_PERMANENTLY;

        public static BusinessStatus of(String businessStatus) {
            return switch (businessStatus) {
                case "OPERATIONAL" -> OPERATIONAL;
                case "CLOSED_TEMPORARILY" -> CLOSED_TEMPORARILY;
                case "CLOSED_PERMANENTLY" -> CLOSED_PERMANENTLY;
                default -> BUSINESS_STATUS_UNSPECIFIED;
            };

        }
    }
}