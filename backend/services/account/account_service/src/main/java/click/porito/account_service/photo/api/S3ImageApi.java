package click.porito.account_service.photo.api;

import click.porito.managed_travel.domain.api.ImageApi;
import click.porito.managed_travel.domain.exception.ImageTypeNotSupportedException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * S3 를 이용한 이미지 처리 서비스
 */
@RefreshScope
@Service
@RequiredArgsConstructor
public class S3ImageApi implements ImageApi {

    @Value("${spring.cloud.aws.s3.profile-img-bucket}")
    protected String BUCKET_NAME;
    protected Duration expiration = Duration.ofMinutes(5);

    private final S3Template s3Template;

    private Long getCurrentUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(name);
    }

    @Override
    @PreAuthorize("authentication.name == #userId")
    public URL createImgPutUri(String userId, String filename) {
        String mimeType = KeyManager.getMimeType(filename);
        String key = KeyManager.getProfileUploadKey(userId, mimeType);
        return s3Template.createSignedPutURL(BUCKET_NAME, key, expiration, null, mimeType);
    }

    @Override
    @PreAuthorize("authentication.name == #userId")
    public void deleteImg(String userId) {
        String key = KeyManager.getProfileImageKey(userId);
        //if key exists, delete it
        s3Template.deleteObject(BUCKET_NAME, key);
    }


    static class KeyManager {


        private final static String IMG_TEMPORARY_PATH = "users/%s/temp/profile_img.%s";
        private final static String IMG_PATH = "users/%s/profile/img.png";
        private final static Pattern PATTERN = Pattern.compile(".*\\.(jpg|jpeg|png|gif)$", Pattern.CASE_INSENSITIVE);
        private final static Map<String, String> ALLOWED_FILE_TYPE =
                Map.of(
                        "image/jpeg", "jpg",
                        "image/png", "png",
                        "image/gif", "gif"
                );



        public static String getProfileUploadKey(String userId, String mimeType) {
            String extension = ALLOWED_FILE_TYPE.get(mimeType);
            return String.format(IMG_TEMPORARY_PATH, userId, extension);
        }

        public static String getProfileImageKey(String userId) {
            return String.format(IMG_PATH, userId);
        }


        public static String getMimeType(String filename) {

            Matcher matcher = PATTERN.matcher(filename);
            if (!matcher.matches()) {
                throw new ImageTypeNotSupportedException();
            } else {
                return matcher.group(1);
            }
        }

    }



}
