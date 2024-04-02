package click.porito.managed_travel.place.place_service.operation.mapper;

import click.porito.common.util.Mapper;
import click.porito.managed_travel.place.domain.OperationTime;
import click.porito.managed_travel.place.domain.Place;
import click.porito.managed_travel.place.domain.PlaceCategory;
import click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity.CategoryEntity;
import click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity.DayOperationTimeEntity;
import click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity.OperationTimeEntity;
import click.porito.managed_travel.place.place_service.operation.persistence.postgresql.entity.PlaceEntity;
import lombok.RequiredArgsConstructor;
import org.geojson.LngLatAlt;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class MapperConfig {
    private final GeometryFactory geometryFactory;

    @Bean
    Mapper<org.locationtech.jts.geom.Point, org.geojson.Point> postGisPointToGeoJsonPointMapper() {
        return new Mapper<org.locationtech.jts.geom.Point, org.geojson.Point>() {
            @Override
            protected org.geojson.Point mapInternal(org.locationtech.jts.geom.Point source) {
                return new org.geojson.Point(
                        source.getX(),
                        source.getY()
                );
            }
        };
    }

    @Bean
    Mapper<org.geojson.Point, org.locationtech.jts.geom.Point> geoJsonPointToPostGisPointMapper() {
        return new Mapper<org.geojson.Point, org.locationtech.jts.geom.Point>() {
            @Override
            protected org.locationtech.jts.geom.Point mapInternal(org.geojson.Point source) {
                LngLatAlt coordinates = source.getCoordinates();
                return geometryFactory.createPoint(new Coordinate(
                        coordinates.getLongitude(),
                        coordinates.getLatitude()
                ));
            }
        };
    }

    @Bean
    Mapper<org.locationtech.jts.geom.Polygon, org.geojson.Polygon> postGisPolygonToGeoJsonPolygon() {
        return new Mapper<Polygon, org.geojson.Polygon>() {
            @Override
            protected org.geojson.Polygon mapInternal(Polygon source) {
                List<LngLatAlt> latAltList = Arrays.stream(source.getCoordinates())
                        .map(coord -> new LngLatAlt(coord.x, coord.y))
                        .toList();
                return new org.geojson.Polygon(latAltList);
            }
        };
    }

    @Bean
    Mapper<org.geojson.Polygon, org.locationtech.jts.geom.Polygon> geoJsonPolygonToPostGisPolygon() {
        return new Mapper<org.geojson.Polygon, org.locationtech.jts.geom.Polygon>() {
            @Override
            protected org.locationtech.jts.geom.Polygon mapInternal(org.geojson.Polygon source) {
                List<LngLatAlt> coordinates = source.getCoordinates().get(0);
                Coordinate[] coords = coordinates.stream()
                        .map(latLon -> new Coordinate(latLon.getLongitude(), latLon.getLatitude()))
                        .toArray(Coordinate[]::new);
                return geometryFactory.createPolygon(coords);
            }
        };
    }

    @Bean
    public Mapper<CategoryEntity, PlaceCategory> categoryEntityToPlaceCategoryMapper() {
        return new Mapper<CategoryEntity, PlaceCategory>() {
            @Override
            protected PlaceCategory mapInternal(CategoryEntity source) {
                return source.getCategory();
            }
        };
    }

    @Bean
    public Mapper<DayOperationTimeEntity, OperationTime.DayOperationTime> dayOperationTimeEntityDayOperationTimeMapper() {
        return new Mapper<DayOperationTimeEntity, OperationTime.DayOperationTime>() {
            @Override
            protected OperationTime.DayOperationTime mapInternal(DayOperationTimeEntity source) {
                OperationTime.DayOperationTime dayOperationTime = new OperationTime.DayOperationTime();
                dayOperationTime.setDayOperationTimeId(source.getDayOperationTimeId());
                dayOperationTime.setStartTime(source.getStartTime());
                dayOperationTime.setEndTime(source.getEndTime());
                dayOperationTime.setNextDayLinked(source.getNextDayLinked());
                return dayOperationTime;
            }
        };
    }

    @Bean
    public Mapper<OperationTimeEntity, OperationTime> operationTimeEntityToOperationTimeMapper(
            Mapper<DayOperationTimeEntity, OperationTime.DayOperationTime> dayOperationTimeEntityToDayOperationTimeMapper
    ) {
        return new Mapper<OperationTimeEntity, OperationTime>() {
            @Override
            protected OperationTime mapInternal(OperationTimeEntity source) {
                OperationTime operationTime = new OperationTime();
                operationTime.setOperationTimeId(source.getOperationTimeId());
                operationTime.setStartDate(source.getStartDate());
                operationTime.setEndDate(source.getEndDate());
                List<OperationTime.DayOperationTime> dayOperationTimes = dayOperationTimeEntityToDayOperationTimeMapper.map(source.getDayOperationTimeEntities());
                operationTime.setDayOperationTimes(dayOperationTimes);
                return operationTime;
            }
        };
    }


    @Bean

    public Mapper<PlaceEntity, Place> placeEntityToPlaceMapper(
            Mapper<org.locationtech.jts.geom.Point, org.geojson.Point> postGisPointToGeoJsonPointMapper,
            Mapper<org.locationtech.jts.geom.Polygon, org.geojson.Polygon> postGisPolygonToGeoJsonPolygon,
            Mapper<CategoryEntity, PlaceCategory> categoryEntityToPlaceCategoryMapper,
            Mapper<OperationTimeEntity, OperationTime> operationTimeEntityToOperationTimeMapper
    ) {
        return new Mapper<PlaceEntity, Place>() {
            @Override
            protected Place mapInternal(PlaceEntity source) {
                Place place = new Place();
                place.setPlaceId(source.getPlaceId());
                place.setName(source.getName());
                place.setKeywords(source.getKeywords());
                place.setAddress(source.getAddress());
                place.setPostalCode(source.getPostalCode());
                place.setPhoneNumber(source.getPhoneNumber());
                place.setWebsite(source.getWebsite());
                place.setSummary(source.getSummary());
                org.geojson.Point point = postGisPointToGeoJsonPointMapper.map(source.getLocation());
                place.setLocation(point);
                org.geojson.Polygon polygon = postGisPolygonToGeoJsonPolygon.map(source.getBoundary());
                place.setBoundary(polygon);
                List<PlaceCategory> placeCategory = categoryEntityToPlaceCategoryMapper.map(source.getCategories());
                place.setCategories(placeCategory);
                place.setCreatedAt(source.getCreatedAt());
                place.setUpdatedAt(source.getUpdatedAt());
                place.setIsGooglePlace(source.getGooglePlaceId() != null);
                List<OperationTime> operationTime = operationTimeEntityToOperationTimeMapper.map(source.getOperationTimes());
                place.setOperationTimes(operationTime);
                return place;
            }
        };
    }

}
