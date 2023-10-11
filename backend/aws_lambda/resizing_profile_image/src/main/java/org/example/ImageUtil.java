package org.example;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {


    public static void process(ProfileImageEvent event, int squareHeight, LambdaLogger logger) throws IOException {
        S3Client s3Client = null;
        InputStream inputStream = null;
        try {
            //Download the image from S3 into a stream
            logger.log(String.format("Downloading from: %s/%s \n", event.getSrcBucket(), event.getSrcKey()));
            s3Client = S3Client.builder().build();
            inputStream = getObject(s3Client, event.getSrcBucket(), event.getSrcKey());

            //Read the source image and resize it
            logger.log(String.format("Processing Start : %s \n", event.getSrcKey()));
            BufferedImage originalImage = ImageIO.read(inputStream);
            BufferedImage newImage = resizeAndCrop(originalImage, squareHeight);

            //Write the resized image to output stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(newImage, "jpg", outputStream);

            //Upload the resized image to S3
            logger.log(String.format("Uploading to: %s/%s \n", event.getDstBucket(), event.getDstKey()));
            putObject(s3Client, outputStream, event.getDstBucket() , event.getDstKey());
        } catch (Throwable e){
            throw new RuntimeException(String.format(
                    "Error Occurred While Processing Image (%s)", event.toString()), e);
        } finally {
            if (s3Client != null) s3Client.close();
            if (inputStream != null) inputStream.close();
        }
    }

    protected static BufferedImage resizeAndCrop(BufferedImage originalImage, int squareLength) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Determine the ratio to maintain aspect ratio
        double ratio = (double) squareLength / Math.min(originalWidth, originalHeight);

        // Calculate the new dimensions while maintaining the aspect ratio
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        // Crop to the specified length x length
        int newLength = Math.min(newWidth, newHeight);
        int x = (newWidth - newLength) / 2;
        int y = (newHeight - newLength) / 2;
        return resizedImage.getSubimage(x, y, newLength, newLength);
    }

    private static InputStream getObject(S3Client s3Client, String srcBucket, String srcKey) {
        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(srcBucket)
                .key(srcKey)
                .build());
    }

    private static void putObject(S3Client s3Client, ByteArrayOutputStream outputStream, String bucketName,
                                  String key) throws AwsServiceException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Length", Integer.toString(outputStream.size()));
        metadata.put("Content-Type", "image/jpeg");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .metadata(metadata)
                .build();

        // Uploading to S3 destination bucket
        s3Client.putObject(putObjectRequest,
                RequestBody.fromBytes(outputStream.toByteArray()));

    }
}
