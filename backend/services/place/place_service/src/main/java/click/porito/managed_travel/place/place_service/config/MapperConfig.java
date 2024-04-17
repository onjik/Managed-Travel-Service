package click.porito.managed_travel.place.place_service.config;

import click.porito.common.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.geojson.LngLatAlt;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MapperConfig {
    private final GeometryFactory geometryFactory;

    @Bean
    public Mapper<org.geojson.Point, Point> geoJsonPointToJtsPointMapper(
            Mapper<LngLatAlt, Coordinate> lngLatAltToCoordinateMapper
    ) {
        return new Mapper<org.geojson.Point, Point>() {
            @Override
            protected Point mapInternal(org.geojson.Point source) {
                Coordinate coordinate = lngLatAltToCoordinateMapper.map(source.getCoordinates());
                return geometryFactory.createPoint(coordinate);
            }
        };
    }

    @Bean
    public Mapper<Point, org.geojson.Point> jtsPointToGeoJsonPointMapper() {
        return new Mapper<Point, org.geojson.Point>() {
            @Override
            protected org.geojson.Point mapInternal(Point source) {
                return new org.geojson.Point(source.getX(), source.getY());
            }
        };
    }

    @Bean
    public Mapper<org.geojson.Polygon,Polygon> geoJsonPolygonToJtsPolygonMapper(
            Mapper<LngLatAlt, Coordinate> lngLatAltToCoordinateMapper
    ) {
        return new Mapper<org.geojson.Polygon, Polygon>() {
            @Override
            protected Polygon mapInternal(org.geojson.Polygon source) {
                List<List<LngLatAlt>> coordinates = source.getCoordinates();
                if (coordinates.size() > 1) {
                    throw new UnsupportedOperationException("Only single polygon is supported");
                } else if (coordinates.isEmpty()) {
                    throw new IllegalArgumentException("Empty polygon");
                }
                List<LngLatAlt> first = coordinates.get(0);
                Coordinate[] array = first.stream()
                        .map(lngLatAltToCoordinateMapper::map)
                        .toArray(Coordinate[]::new);
                return geometryFactory.createPolygon(array);
            }
        };
    }

    @Bean
    public Mapper<Polygon, org.geojson.Polygon> jtsPolygonToGeoJsonPolygonMapper() {
        return new Mapper<Polygon, org.geojson.Polygon>() {
            @Override
            protected org.geojson.Polygon mapInternal(Polygon source) {
                LngLatAlt[] array = Arrays.stream(source.getCoordinates())
                        .map(coordinate -> new LngLatAlt(coordinate.x, coordinate.y))
                        .toArray(LngLatAlt[]::new);
                return new org.geojson.Polygon(array);
            }
        };
    }

    @Bean
    public Mapper<LngLatAlt, Coordinate> lngLatAltToCoordinateMapper() {
        return new Mapper<LngLatAlt, Coordinate>() {
            @Override
            protected Coordinate mapInternal(LngLatAlt source) {
                return new Coordinate(source.getLongitude(), source.getLatitude());
            }
        };
    }


}
