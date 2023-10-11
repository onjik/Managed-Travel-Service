package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private static String REGEX = "(.*)\\.([^\\.]*)";

    private Gson gson = new GsonBuilder().create();

    private LambdaLogger logger;


    @Override
    public String handleRequest(S3Event input, Context context) {
        //metadata
        logger = context.getLogger();


        //extract properties from S3Event
        var records = input.getRecords();
        List<ProfileImageEvent> events = new ArrayList<>();
        for (var record : records){
            try {
                events.add(createProfileEvent(record));
            } catch (IllegalArgumentException e) {
                //just skip
                logger.log(e.getMessage()+ "\n");
            }
        }

        //processing start logging
        int totalCount = events.size();
        int processedCount = 0;

        List<ProfileImageEvent> successfulEvents = events.parallelStream()
                .map(event -> {
                    try {
                        logger.log(String.format("Start processing image : %s \n", event.getSrcKey()));
                        ImageUtil.process(event, OUTPUT_WIDTH_HEIGHT, logger);
                        logger.log(String.format("Processing finished : %s \n", event.getSrcKey()));
                    } catch (Exception e) {
                        logger.log(e.getMessage()+ "\n");
                        return null;
                    }
                    return event;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (successfulEvents.isEmpty()) {
            logger.log("No image processed\n");
            System.exit(1);
        }


        String result = gson.toJson(successfulEvents);

        //TODO implement sendMessage to event Broker
        //sendMessage(result);

        return result;

    }

    protected ProfileImageEvent createProfileEvent(S3EventNotification.S3EventNotificationRecord record) throws IllegalArgumentException{
        // default metadata
        String srcBucket = record.getS3().getBucket().getName();
        String srcKey = record.getS3().getObject().getKey();

        // extract keyRoot and imageType
        Matcher matcher = Pattern.compile(REGEX).matcher(srcKey);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Skipping invalid key : Cannot Parse Extension From -> " + srcKey);
        }
        String keyRoot = matcher.group(1);
        String imageType = matcher.group(2);
        logger.log(String.format("keyRoot : %s, imageType : %s \n", keyRoot, imageType));

        // check imageType
        final List<String> allowedExtension = List.of("jpg", "jpeg", "png", "gif");
        if (!allowedExtension.contains(imageType)) {
            throw new IllegalArgumentException("Skipping not supported extension image : " + srcKey);
        }

        // create dstKey
        String dstKey = keyRoot + ".jpg";
        return new ProfileImageEvent(srcBucket, srcKey, UPLOAD_BUCKET_NAME, dstKey);
    }

    private void sendMessage(String msg) {
//        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
//                .withRegion(Regions.AP_NORTHEAST_2)
//                .build();
//
//        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
//
//        SendMessageResult smr = sqs.sendMessage(new SendMessageRequest()
//                .withQueueUrl(queueUrl)
//                .withMessageBody(msg));
//
//        return "SendMessage succeeded with messageId " + smr.getMessageId()
//                + ", sequence number " + smr.getSequenceNumber() + "\n";
    }
}
