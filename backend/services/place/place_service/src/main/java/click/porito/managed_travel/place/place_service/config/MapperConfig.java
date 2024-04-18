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




}
