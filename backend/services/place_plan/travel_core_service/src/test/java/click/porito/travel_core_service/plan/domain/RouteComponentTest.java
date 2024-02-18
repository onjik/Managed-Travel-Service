package click.porito.travel_core_service.plan.domain;

import click.porito.managed_travel.plan.Day;
import click.porito.managed_travel.plan.RouteComponent;
import click.porito.managed_travel.plan.WayPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RouteComponentTest {

    @Nested
    @DisplayName("Jackson DEDUCTION 테스트")
    class JacksonDeductionTest {
        private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        @Test
        void test() throws JsonProcessingException {
            // given
            String json = """
                    {
                      "dayId": "1",
                      "wayPoints": [
                        {
                          "waypointId": "1",
                          "placeId": "1",
                          "memo": "1",
                          "time": "10:00"
                        },
                        {
                          "waypointId": "2",
                          "placeId": "2",
                          "memo": "2",
                          "time": "11:00"
                        }
                      ]
                    }
                    """;
            // when
            RouteComponent routeComponentPointer = objectMapper.readValue(json, RouteComponent.class);
            // then
            Assertions.assertInstanceOf(Day.class, routeComponentPointer);
            Day day = (Day) routeComponentPointer;
            day.getWayPoints().forEach(wayPoint -> Assertions.assertInstanceOf(WayPoint.class, wayPoint));
        }
    }

}