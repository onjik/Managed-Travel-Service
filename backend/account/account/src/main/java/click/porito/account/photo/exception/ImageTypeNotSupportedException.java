package click.porito.account.photo.exception;

import click.porito.account.photo.api.application.S3ImageApi;
import lombok.Getter;

/**
 * 지원하지 않는 이미지 타입을 제시하였을 때 이를 알리는 예외
 * {@link S3ImageApi#createImgPutUri(String)}
 */
@Getter
public class ImageTypeNotSupportedException extends RuntimeException {
    private final String unsupportedMimeType;
    public ImageTypeNotSupportedException(String unsupportedMimeType) {
        this.unsupportedMimeType = unsupportedMimeType;
    }

}
