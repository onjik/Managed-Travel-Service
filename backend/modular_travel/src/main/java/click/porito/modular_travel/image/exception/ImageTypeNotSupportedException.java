package click.porito.modular_travel.image.exception;

import click.porito.modular_travel.image.management.S3ImageInternalManagement;
import lombok.Getter;

/**
 * 지원하지 않는 이미지 타입을 제시하였을 때 이를 알리는 예외
 * {@link S3ImageInternalManagement#createAccountImgPutUri(String)}
 */
@Getter
public class ImageTypeNotSupportedException extends RuntimeException {
    private final String unsupportedMimeType;
    public ImageTypeNotSupportedException(String unsupportedMimeType) {
        this.unsupportedMimeType = unsupportedMimeType;
    }

}
