package click.porito.modular_travel.place;

public interface GooglePlace extends Place {

    String getPrimaryType();
    LocalizedString getPrimaryTypeDisplayName();
    String getShortFormattedAddress();
    String getIconBackgroundColor();
    String getIconMaskBaseUri();
    String getGoogleMapsUri();
}
