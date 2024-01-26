import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.ProfileImageEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class GsonTest {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    void mapConvertTest() {
        // given
        HashMap<String, Object> map = new HashMap<>();
        map.put("isAllRequestsSuccessful", true);
        map.put("totalCount", 10);
        map.put("processedCount", 8);
        // when
        String json = gson.toJson(map);
        // then
        String result = "{\n" +
                "  \"isAllRequestsSuccessful\": true,\n" +
                "  \"processedCount\": 8,\n" +
                "  \"totalCount\": 10\n" +
                "}";
        System.out.println(json);
        Assertions.assertEquals(result, json);
    }

    @Test
    void eventConvertTest() {
        // given
        ProfileImageEvent event1 = new ProfileImageEvent("srcBucket", "srcKey", "dstBucket", "dstKey");
        ProfileImageEvent event2 = new ProfileImageEvent("srcBucket", "srcKey", "dstBucket", "dstKey");
        ProfileImageEvent event3 = new ProfileImageEvent("srcBucket", "srcKey", "dstBucket", "dstKey");
        List<ProfileImageEvent> eventList = List.of(event1, event2, event3);
        // when
        String json = gson.toJson(eventList);
        // then
        String result = "[\n" +
                "  {\n" +
                "    \"srcBucket\": \"srcBucket\",\n" +
                "    \"srcKey\": \"srcKey\",\n" +
                "    \"dstBucket\": \"dstBucket\",\n" +
                "    \"dstKey\": \"dstKey\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"srcBucket\": \"srcBucket\",\n" +
                "    \"srcKey\": \"srcKey\",\n" +
                "    \"dstBucket\": \"dstBucket\",\n" +
                "    \"dstKey\": \"dstKey\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"srcBucket\": \"srcBucket\",\n" +
                "    \"srcKey\": \"srcKey\",\n" +
                "    \"dstBucket\": \"dstBucket\",\n" +
                "    \"dstKey\": \"dstKey\"\n" +
                "  }\n" +
                "]";
        Assertions.assertEquals(result, json);
    }
}
