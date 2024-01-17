package click.porito.travel_core.plan.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;

class RestDtoMappingTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeAll() {
        // localtime 지원하도록 설정
        objectMapper.registerModule(new JavaTimeModule());
    }



    @Nested
    class RouteComponentPutRequestTest{

        @Test
        void waypointPutRequest() throws JsonProcessingException {
            String json = """
                    {
                      "wayPointId": "string",
                      "placeId": "string",
                      "memo": "string",
                      "time": "12:00"
                    }
                    """;
            RouteComponentRequest type = objectMapper.readValue(json, RouteComponentRequest.class);

            Assertions.assertInstanceOf(WayPointUpdateRequest.class, type);
        }

        @Test
        void dayPutRequest() throws JsonProcessingException {
            String json = """
                    {
                      "wayPoints": [
                        {
                          "wayPointId": "string",
                          "placeId": "string",
                          "memo": "string",
                          "time": "12:00"
                        }
                      ]
                    }
                    """;
            RouteComponentRequest type = objectMapper.readValue(json, RouteComponentRequest.class);

            Assertions.assertInstanceOf(DayUpdateRequest.class, type);
        }
    }


    @Nested
    class RouteComponentRequestTest{

        @Test
        void waypointPutRequest() throws JsonProcessingException {
            String json = """
                    {
                      "wayPointId": "string",
                      "placeId": "string",
                      "memo": "string",
                      "time": "12:00"
                    }
                    """;
            RouteComponentRequest type = objectMapper.readValue(json, RouteComponentRequest.class);

            Assertions.assertInstanceOf(WayPointUpdateRequest.class, type);
        }

        @Test
        void dayPutRequest() throws JsonProcessingException {
            String json = """
                    {
                      "dayId": "string",
                      "wayPoints": [
                        {
                          "wayPointId": "string",
                          "placeId": "string",
                          "memo": "string",
                          "time": "12:00"
                        }
                      ]
                    }
                    """;
            RouteComponentRequest type = objectMapper.readValue(json, RouteComponentRequest.class);

            Assertions.assertInstanceOf(DayUpdateRequest.class, type);
        }
    }



}