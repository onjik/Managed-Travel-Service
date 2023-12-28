package click.porito.place_service.external_api.google_api;

public interface GooglePlacePhotoApi {
    /**
     * 주어진 이름으로 부터 사진 uri를 반환한다.
     * @param photoName 전달된 레퍼런스 이름
     * @param maxWidthPx 1~ 4800
     * @param maxHeightPx 1~ 4800
     * @return photoUri
     */
    String photoUri(String photoName, int maxWidthPx, int maxHeightPx);
}
