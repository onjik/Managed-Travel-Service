package click.porito.travel_plan_service.place.dto;

public enum BusinessStatus {
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
