package click.porito.travel_core.place.operation.adapter.google_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GooglePhotoReference {
    private String name;
    private int heightPx;
    private int widthPx;
    private List<GoogleAuthorAttribution> authorAttributions;

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class GoogleAuthorAttribution {
        private String displayName;
        private String uri;
        private String photoUri;
    }
}



