package click.porito.managed_travel.place.domain.util;

import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.domain.request.command.UserPlaceCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SingleChunkPolygonValidatorTest {

    static ValidatorFactory factory;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    @DisplayName("두개의 덩어리로 이루어진 폴리곤은 유효하지 않다.")
    void testInvalid() {

        // given
        UserPlaceCreateRequest request = new UserPlaceCreateRequest();
        request.setName("test");
        request.setLocation(new Point(0, 0));
        Polygon polygon = new Polygon();
        polygon.add(List.of(new LngLatAlt(0, 0), new LngLatAlt(1, 1)));
        polygon.add(List.of(new LngLatAlt(2, 2), new LngLatAlt(3, 3)));
        request.setBoundary(polygon);
        request.setCategories(List.of(PlaceCategory.ACCOMMODATION));

        // when
        var violations = factory.getValidator().validate(request);
        var optional = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("boundary"))
                .findFirst();

        // then
        assertTrue(optional.isPresent());
    }

    @Test
    @DisplayName("한개의 덩어리로 이루어진 폴리곤은 유효하다.")
    void testValid() {

        // given
        UserPlaceCreateRequest request = new UserPlaceCreateRequest();
        request.setName("test");
        request.setLocation(new Point(0, 0));
        Polygon polygon = new Polygon();
        polygon.add(List.of(new LngLatAlt(0, 0), new LngLatAlt(1, 1), new LngLatAlt(2, 2), new LngLatAlt(0, 0)));
        request.setBoundary(polygon);
        request.setCategories(List.of(PlaceCategory.ACCOMMODATION));

        // when
        var violations = factory.getValidator().validate(request);

        // then
        assertTrue(violations.isEmpty());
    }

}