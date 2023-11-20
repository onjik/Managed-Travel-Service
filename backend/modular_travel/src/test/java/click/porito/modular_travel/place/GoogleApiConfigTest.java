package click.porito.modular_travel.place;

import com.google.maps.GeoApiContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class GoogleApiConfigTest {

    @Autowired
    GeoApiContext geoApiContext;

    @Nested
    @DisplayName("geoApiContext()")
    class GeoApiContextTest {
        @Test
        @DisplayName("bean injection")
        void beanInjection() {
            assertNotNull(geoApiContext);
        }

    }

}