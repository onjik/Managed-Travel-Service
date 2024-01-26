package click.porito.account_common.exception;

import click.porito.common.exception.ErrorCode;
import lombok.Getter;


@Getter
public class ImageTypeNotSupportedException extends AccountBusinessException {

    public ImageTypeNotSupportedException(Throwable cause) {
        super(cause, ErrorCode.PROFILE_IMG_TYPE_NOT_SUPPORTED);
    }
    public ImageTypeNotSupportedException() {
        super(ErrorCode.PROFILE_IMG_TYPE_NOT_SUPPORTED);
    }

}
