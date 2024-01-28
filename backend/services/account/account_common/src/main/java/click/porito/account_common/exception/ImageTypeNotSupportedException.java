package click.porito.account_common.exception;

import click.porito.common.exception.ErrorCodes;
import lombok.Getter;


@Getter
public class ImageTypeNotSupportedException extends AccountBusinessException {

    public ImageTypeNotSupportedException(Throwable cause) {
        super(cause, ErrorCodes.PROFILE_IMG_TYPE_NOT_SUPPORTED);
    }
    public ImageTypeNotSupportedException() {
        super(ErrorCodes.PROFILE_IMG_TYPE_NOT_SUPPORTED);
    }

}
