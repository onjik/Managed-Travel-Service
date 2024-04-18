package click.porito.managed_travel.place.place_service.util;

import click.porito.common.util.Mapper;
import org.geojson.LngLatAlt;
import org.locationtech.jts.geom.*;

import java.util.Arrays;
import java.util.List;

public abstract class GeoUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public static Mapper<org.geojson.Point, Point> geoJsonPointToJtsPointMapper() {
        return new Mapper<org.geojson.Point, Point>() {
            @Override
            protected Point mapInternal(org.geojson.Point source) {
                Coordinate coordinate = lngLatAltToCoordinateMapper().map(source.getCoordinates());
                return geometryFactory.createPoint(coordinate);
            }
        };
    }

    public static Mapper<Point, org.geojson.Point> jtsPointToGeoJsonPointMapper() {
        return new Mapper<Point, org.geojson.Point>() {
            @Override
            protected org.geojson.Point mapInternal(Point source) {
                return new org.geojson.Point(source.getX(), source.getY());
            }
        };
    }

    public static Mapper<org.geojson.Polygon, Polygon> geoJsonPolygonToJtsPolygonMapper() {
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
                        .map(lngLatAlt -> new Coordinate(lngLatAlt.getLongitude(), lngLatAlt.getLatitude()))
                        .toArray(Coordinate[]::new);
                return geometryFactory.createPolygon(array);
            }
        };
    }

    public static Mapper<Polygon, org.geojson.Polygon> jtsPolygonToGeoJsonPolygonMapper() {
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


    public static Mapper<LngLatAlt, Coordinate> lngLatAltToCoordinateMapper() {
        return new Mapper<LngLatAlt, Coordinate>() {
            @Override
            protected Coordinate mapInternal(LngLatAlt source) {
                return new Coordinate(source.getLongitude(), source.getLatitude());
            }
        };
    }
}
