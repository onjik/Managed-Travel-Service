package click.porito.modular_travel.image;

import java.net.URL;

/**
 * 이미지 처리 서비스에 대한 추상화 인터페이스
 */
public interface ImageInternalManagement {
    URL createAccountImgPutUri(String filename);

    void deleteAccountImg();
}
