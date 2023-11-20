package click.porito.modular_travel.place;

import com.google.maps.model.PlaceEditorialSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedString {
    private String text;
    private Locale languageCode;

    public static LocalizedString of(PlaceEditorialSummary editorialSummary) {
        Locale locale;
        if (editorialSummary.language == null) {
            locale = null;
        } else {
            locale = new Locale(editorialSummary.language);
        }
        return new LocalizedString(editorialSummary.overview, locale);
    }
}
