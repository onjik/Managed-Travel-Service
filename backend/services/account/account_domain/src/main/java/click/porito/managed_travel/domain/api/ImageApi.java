package click.porito.managed_travel.domain.api;

import java.net.URL;

/**
 * 이미지 처리 서비스에 대한 추상화 인터페이스
 */
public interface ImageApi {
    URL createImgPutUri(String userId,String filename);

    void deleteImg(String userId);
}
