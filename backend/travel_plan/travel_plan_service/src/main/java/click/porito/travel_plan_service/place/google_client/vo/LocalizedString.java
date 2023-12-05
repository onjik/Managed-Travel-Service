package click.porito.travel_plan_service.place.google_client.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedString {
    private String text;
    private Locale languageCode;

    public Map<Locale, String> toMap() {
        return Map.of(languageCode, text);
    }

}
