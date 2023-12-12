package click.porito.place_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    String name;
    private int heightPx;
    private int widthPx;
    private AuthorAttribution[] authorAttributions;

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class AuthorAttribution {
        private String displayName;
        private String uri;
        private String photoUri;
    }
}
