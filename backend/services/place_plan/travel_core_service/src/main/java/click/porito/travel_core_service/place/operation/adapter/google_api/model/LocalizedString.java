package click.porito.travel_core_service.place.operation.adapter.google_api.model;

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

}
