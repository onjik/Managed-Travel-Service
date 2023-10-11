package org.example;

public class ProfileImageEvent {
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
