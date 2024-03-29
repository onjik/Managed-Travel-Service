package click.porito.managed_travel.place.domain.api.request;

import lombok.Data;

@Data
public class PhotoPutCommand {
    private byte[] photo;
    private String contentType;
    private String originalFileName;
    private String sourceReference;
}
