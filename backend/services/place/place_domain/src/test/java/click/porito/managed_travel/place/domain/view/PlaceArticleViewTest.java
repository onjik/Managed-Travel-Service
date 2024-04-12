package click.porito.managed_travel.place.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class PlaceArticleViewTest {

    @Test
    void jsonFormatTest() throws JsonProcessingException {
        //given
        PlaceArticleView placeArticleView = new PlaceArticleView();
        // 2024-01-01T00:00:00Z
        String expected = "2024-01-01T00:00:00Z";
        Instant instant = Instant.parse(expected);
        placeArticleView.setCreatedAt(instant);

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));

        String json = objectMapper.writeValueAsString(placeArticleView);
        String read = JsonPath.read(json, "$.createdAt");

        //then
        assertEquals(expected, read);
        System.out.println(json);


    }

}