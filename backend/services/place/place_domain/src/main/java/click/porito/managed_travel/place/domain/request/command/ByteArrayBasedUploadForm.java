package click.porito.managed_travel.place.domain.request.command;

import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.util.Assert;

import java.io.*;

public class ByteArrayBasedUploadForm implements MediaUploadForm {
    private final byte[] bytes;
    private final String contentType;
    @Setter
    private String originalFilename;
    @Setter
    private String name;

    public ByteArrayBasedUploadForm(byte[] bytes, String contentType) {
        Assert.notNull(bytes, "bytes must not be null");
        Assert.notNull(contentType, "contentType must not be null");
        Assert.isTrue(!contentType.isEmpty(), "contentType must not be empty");
        this.bytes = bytes;
        this.contentType = contentType;
    }

    public ByteArrayBasedUploadForm(byte[] bytes, String contentType, String originalFilename, String name) {
        this(bytes, contentType);
        this.originalFilename = originalFilename;
        this.name = name;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }
}
