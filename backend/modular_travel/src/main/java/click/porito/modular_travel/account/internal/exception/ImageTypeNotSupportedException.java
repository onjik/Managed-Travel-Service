package click.porito.modular_travel.account.internal.exception;

import click.porito.modular_travel.account.internal.service.S3ImageService;
import lombok.Getter;

/**
 * 지원하지 않는 이미지 타입을 제시하였을 때 이를 알리는 예외
 * {@link S3ImageService#createAccountImgPutUri(String)}
 */
@Getter
public class ImageTypeNotSupportedException extends AccountBusinessException{
    private final String unsupportedMimeType;
    public ImageTypeNotSupportedException(String unsupportedMimeType) {
        this.unsupportedMimeType = unsupportedMimeType;
    }

}
