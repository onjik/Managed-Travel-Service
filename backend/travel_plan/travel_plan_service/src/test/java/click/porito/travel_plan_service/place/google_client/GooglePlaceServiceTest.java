package click.porito.travel_plan_service.place.google_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@SpringBootTest
class GooglePlaceServiceTest {

    @Autowired
    RestClient.Builder restClientBuilder;

    @Autowired
    GoogleApiContext context;

    private GooglePlaceService googlePlaceService;
    private MockServerRestClientCustomizer customizer;

    @BeforeEach
    void setUp() {
        customizer = new MockServerRestClientCustomizer();
        customizer.customize(restClientBuilder);
        googlePlaceService = new GooglePlaceService(restClientBuilder, context);
    }

    @Test
    void inject() {
        assertNotNull(restClientBuilder);
    }


    @DisplayName("getPlaceDetail")
    @Nested
    class getPlace{
        @Test
        @DisplayName("google place detail api를 통해 place 정보를 가져온다.")
        void getPlaceDetail() {
            final var placeId = "mocked_place_id";

            customizer.getServer()
                    .expect(request -> {
                        Pattern pattern = Pattern.compile("https://places.googleapis.com/v1/places/(.*)");
                        var uri = request.getURI().toString();
                        var matcher = pattern.matcher(uri);
                        assertTrue(matcher.matches());
                    })
                    .andRespond(withSuccess("""
                            {\
                                "id": "%s"
                            }
                            """.formatted(placeId), MediaType.APPLICATION_JSON));
            var place = googlePlaceService.getPlace(placeId);
            assertNotNull(place);
            assertEquals(placeId, place.id());
        }

        @Test
        @DisplayName("google place detail api를 통해 place 정보를 가져올 때, 잘못된 placeId를 입력하면 예외가 발생한다.")
        void getPlaceDetailWithInvalidPlaceId() {
            final var placeId = "mocked_place_id";
            customizer.getServer()
                    .expect(request -> {
                        Pattern pattern = Pattern.compile("https://places.googleapis.com/v1/places/(.*)");
                        var uri = request.getURI().toString();
                        var matcher = pattern.matcher(uri);
                        assertTrue(matcher.matches());
                    })
                    .andRespond(withBadRequest());
            assertThrows(GoogleApiCallFailedException.class, () -> googlePlaceService.getPlace(placeId));
        }

        @Test
        @DisplayName("google place detail api를 통해 place 정보를 가져올 때, 잘못된 placeId를 입력하면 예외가 발생한다.")
        void getPlaceDetailWithInvalidPlaceId2() {
            final var placeId = "mocked_place_id";
            customizer.getServer()
                    .expect(request -> {
                        Pattern pattern = Pattern.compile("https://places.googleapis.com/v1/places/(.*)");
                        var uri = request.getURI().toString();
                        var matcher = pattern.matcher(uri);
                        assertTrue(matcher.matches());
                    })
                    .andRespond(withBadRequest());
            assertThrows(GoogleApiCallFailedException.class, () -> googlePlaceService.getPlace(placeId));
        }


    }

}