package click.porito.managed_travel.place.domain.request.command;

import jakarta.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public interface MediaUploadForm {
    InputStream getInputStream() throws IOException;
    @Nullable
    String getName();
    @Nullable
    String getOriginalFilename();
    String getContentType();
    boolean isEmpty();
    long getSize();
    byte[] getBytes() throws IOException;
}
