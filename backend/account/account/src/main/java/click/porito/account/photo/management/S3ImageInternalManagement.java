package click.porito.account.photo.management;

import click.porito.account.account.exception.InvalidAuthenticationException;
import click.porito.account.photo.ImageInternalManagement;
import click.porito.account.photo.exception.ImageTypeNotSupportedException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@Service
@RequiredArgsConstructor
public class S3ImageInternalManagement implements ImageInternalManagement {

    @Value("${spring.cloud.aws.s3.profile-img-bucket}")
    protected String BUCKET_NAME;
    protected Duration expiration = Duration.ofMinutes(5);

    private final S3Template s3Template;

    private Long getCurrentUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(name);
    }

    /**
     * @throws ImageTypeNotSupportedException if the file type is not supported
     * @throws InvalidAuthenticationException if the user id is not valid
     */
    @Override
    public URL createAccountImgPutUri(String filename) {
        Long userId = getCurrentUserId();
        String mimeType = KeyManager.getMimeType(filename);
        String key = KeyManager.getProfileUploadKey(userId, mimeType);
        return s3Template.createSignedPutURL(BUCKET_NAME, key, expiration, null, mimeType);
    }

    @Override
    public void deleteAccountImg() {
        Long userId = getCurrentUserId();
        String key = KeyManager.getProfileImageKey(userId);
        //if key exists, delete it
        s3Template.deleteObject(BUCKET_NAME, key);
    }


    static class KeyManager {


        private final static String IMG_TEMPORARY_PATH = "users/%d/temp/profile_img.%s";
        private final static String IMG_PATH = "users/%d/profile/img.png";
        private final static Pattern PATTERN = Pattern.compile(".*\\.(jpg|jpeg|png|gif)$", Pattern.CASE_INSENSITIVE);
        private final static Map<String, String> ALLOWED_FILE_TYPE =
                Map.of(
                        "image/jpeg", "jpg",
                        "image/png", "png",
                        "image/gif", "gif"
                );



        public static String getProfileUploadKey(Long userId, String mimeType) {
            String extension = ALLOWED_FILE_TYPE.get(mimeType);
            return String.format(IMG_TEMPORARY_PATH, userId, extension);
        }

        public static String getProfileImageKey(Long userId) {
            return String.format(IMG_PATH, userId);
        }


        public static String getMimeType(String filename) {

            Matcher matcher = PATTERN.matcher(filename);
            if (!matcher.matches()) {
                throw new ImageTypeNotSupportedException("not supported file type");
            } else {
                return matcher.group(1);
            }
        }

    }



}