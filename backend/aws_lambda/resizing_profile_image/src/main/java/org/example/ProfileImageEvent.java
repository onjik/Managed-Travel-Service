package org.example;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileImageEvent {
    private static final String REGEX = "(.+\\/)*(.+)(\\..+)$";
    private String srcBucket;
    private String srcKey;
    private String dstBucket;
    private String dstKey;

    @Override
    public String toString() {
        return "ProfileImageEvent{" +
                "srcBucket='" + srcBucket + '\'' +
                ", srcKey='" + srcKey + '\'' +
                ", dstBucket='" + dstBucket + '\'' +
                ", dstKey='" + dstKey + '\'' +
                '}';
    }

    public ProfileImageEvent(String srcBucket, String srcKey, String dstBucket, String dstKey) {
        this.srcBucket = srcBucket;
        this.srcKey = srcKey;
        this.dstBucket = dstBucket;
        this.dstKey = dstKey;
    }

    public static ProfileImageEvent of(
            S3EventNotification.S3EventNotificationRecord record, String UPLOAD_BUCKET_NAME){
        // default metadata
        String srcBucket = record.getS3().getBucket().getName();
        String srcKey = record.getS3().getObject().getKey();

        // extract keyRoot and imageType
        Matcher matcher = Pattern.compile(REGEX).matcher(srcKey);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Skipping invalid key : Cannot Parse Extension From -> " + srcKey);
        }
        String path = matcher.group(1);
        String fileName = matcher.group(2);
        String extension = matcher.group(3);

        // check imageType
        final List<String> allowedExtension = List.of(".jpg", ".jpeg", ".png");
        if (!allowedExtension.contains(extension)) {
            throw new IllegalArgumentException("Skipping not supported extension image : " + extension);
        }

        // create dstKey
        String dstKey = Optional.ofNullable(path).orElse("") + "profile_img.jpg";
        return new ProfileImageEvent(srcBucket, srcKey, UPLOAD_BUCKET_NAME, dstKey);
    }

    public String getSrcBucket() {
        return srcBucket;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public String getDstBucket() {
        return dstBucket;
    }

    public String getDstKey() {
        return dstKey;
    }

}
