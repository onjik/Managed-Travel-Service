package org.example;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {

    public static void deleteObject(S3Client s3Client, String bucketName, String key) {
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key));
    }

    public static InputStream getObject(S3Client s3Client, String srcBucket, String srcKey) {
        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(srcBucket)
                .key(srcKey)
                .build());
    }

    public static void putObject(S3Client s3Client, ByteArrayOutputStream outputStream, String bucketName,
                                  String key) throws AwsServiceException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Length", Integer.toString(outputStream.size()));
        metadata.put("Content-Type", "image/jpeg");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .contentType("image/jpeg")
                .bucket(bucketName)
                .key(key)
                .metadata(metadata)
                .build();

        // Uploading to S3 destination bucket
        s3Client.putObject(putObjectRequest,
                RequestBody.fromBytes(outputStream.toByteArray()));

    }
}
