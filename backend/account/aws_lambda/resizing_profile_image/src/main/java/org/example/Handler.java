package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.event.FailureResult;
import org.example.event.ProcessingResult;
import org.example.event.SuccessResult;
import software.amazon.awssdk.services.s3.S3Client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static org.example.ImageUtil.*;

/**
 * AWS Lambda function 으로 업로드 S3 Bucket 에 이미지가 업로드 되면, 트리거링 됩니다.
 * 1. 트리거
 * 2. 업로드된 이미지를 다운로드 합니다.
 * 3. 이미지를 검증합니다.
 * 4. 이미지를 리사이징 합니다.
 * 5. 리사이징된 이미지를 업로드 합니다.
 * 6. 원래 이미지를 삭제합니다.
 * 7. sqs를 사용하여 이미지가 업로드 되었음을 알립니다.
 */
public class Handler implements RequestHandler<S3Event, String> {

    private final static String UPLOAD_BUCKET_NAME = "travel-service-account-public-test";
    private final static int OUTPUT_WIDTH_HEIGHT = 400;
    private Gson gson = new GsonBuilder().create();

    private LambdaLogger logger;


    private <O> O nullIfException(Callable<O> supplier) {
        try {
            return supplier.call();
        } catch (Exception e) {
            logger.log(e.getMessage()+ "\n");
            return null;
        }
    }


    @Override
    public String handleRequest(S3Event input, Context context) {
        logger = context.getLogger();

        logger.log(input.getRecords().size() + " Image Uploaded Event Accepted \n");
        var results = input.getRecords().parallelStream()
                .map(record -> nullIfException(() -> ProfileImageEvent.of(record, UPLOAD_BUCKET_NAME)))
                .filter(Objects::nonNull) // 아예 안맞는 것들은 처리 시도를 안함
                .peek(event -> logger.log("Processing Start : " + event.toString() + "\n"))
                .map(event -> process(event, OUTPUT_WIDTH_HEIGHT))
                .peek(result -> {
                    if (result instanceof SuccessResult) {
                        logger.log("Processing Success : " + result.getSourceEvent().toString() + "\n");
                    } else if (result instanceof FailureResult) {
                        logger.log("Processing Failure : " + result.getSourceEvent().toString() + "\n");
                    }
                })
                .peek(this::publishResultEvent)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            logger.log("No Image Processed");
            System.exit(1);
        }

        long failCount = results.stream().filter(result -> result instanceof FailureResult).count();
        long successCount = results.size() - failCount;


        String jopReport = String.format("Processing ( %d / %d ) Image -> {success : %d, fail : %d} \n",
                results.size(), input.getRecords().size(), successCount, failCount);
        logger.log(jopReport);


        var returnJsonValue = Map.of("result", results,
                "jobReport", jopReport
        );
        return gson.toJson(returnJsonValue);
    }


    public ProcessingResult process(ProfileImageEvent event, int squareHeight) {
        S3Client s3Client = null;
        InputStream inputStream = null;
        String srcBucket = event.getSrcBucket();
        String srcKey = event.getSrcKey();
        String dstBucket = event.getDstBucket();
        String dstKey = event.getDstKey();
        try {
            //Download the image from S3 into a stream
            logger.log(String.format("Downloading from: %s/%s \n", srcBucket, srcKey));
            s3Client = S3Client.builder().build();
            inputStream = getObject(s3Client, srcBucket, srcKey);

            //Read the source image and resize it
            logger.log(String.format("Processing Start : %s \n", srcKey));
            BufferedImage originalImage = ImageIO.read(inputStream);

            //Resize the image to a square
            logger.log(String.format("Resizing to: %d \n", squareHeight));
            BufferedImage newImage = resizeAndCrop(originalImage, squareHeight);

            //Write the resized image to output stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(newImage, "jpg", outputStream);

            //Upload the resized image to S3
            logger.log(String.format("Uploading to: %s/%s \n", dstBucket, event.getDstKey()));//TODO 변수로 치환
            putObject(s3Client, outputStream, event.getDstBucket() , event.getDstKey());

            //Delete the original image
            logger.log(String.format("Deleting from: %s/%s \n", event.getSrcBucket(), event.getSrcKey()));
            deleteObject(s3Client, event.getSrcBucket(), event.getSrcKey());
        } catch (Throwable e){
            return new FailureResult(event, e, "Error Occurred While Processing Image");
        } finally {
            try {
                if (s3Client != null) s3Client.close();
                if (inputStream != null) inputStream.close();
            } catch (Throwable e){
                //ignore
            }
        }
        return new SuccessResult(event);
    }

    protected BufferedImage resizeAndCrop(BufferedImage originalImage, int squareLength) {
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

    private void publishResultEvent(ProcessingResult result) {
        //TODO 이벤트 전송
    }



}
