package click.porito.travel_core.plan.operation.application.mapper;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.plan.operation.adapter.persistent.entity.DayEntity;
import click.porito.travel_core.plan.operation.adapter.persistent.entity.WayPointEntity;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.domain.RouteComponent;
import click.porito.travel_core.plan.domain.WayPoint;
import click.porito.travel_core.plan.operation.adapter.persistent.entity.EntityRouteComponent;
import click.porito.travel_core.plan.operation.adapter.persistent.entity.PlanEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Configuration(proxyBeanMethods = true)
public class PlanMapperConfig {

    @Bean
    public Mapper<PlanEntity, Plan> toPlan(final Mapper<EntityRouteComponent, RouteComponent> rcvMapper) {
        return new Mapper<>() {
            @Override
            protected Plan mapInternal(PlanEntity source) {
                String planId = source.getPlanId();
                String title = source.getTitle();
                LocalDate startDate = source.getStartDate(); // nullable
                Instant createdAt = source.getCreatedAt();
                Instant updatedAt = source.getUpdatedAt();
                String ownerId = source.getOwnerId();
                String version = Optional.ofNullable(source.getVersion())
                        .map(Object::toString).orElse(null);
                var route = rcvMapper.map(source.getRoute());

                return Plan.builder()
                        .planId(planId)
                        .title(title)
                        .startDate(startDate)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .ownerId(ownerId)
                        .version(version)
                        .route(route)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<Plan,PlanEntity> toDto(final Mapper<RouteComponent,EntityRouteComponent> rcvMapper){
        return new Mapper<Plan, PlanEntity>() {
            @Override
            protected PlanEntity mapInternal(Plan source) {
                String planId = source.getPlanId();
                String title = source.getTitle();
                LocalDate startDate = source.getStartDate(); // nullable
                Instant createdAt = source.getCreatedAt();
                Instant updatedAt = source.getUpdatedAt();
                String ownerId = source.getOwnerId();
                Long version = Optional.ofNullable(source.getVersion())
                        .map(Long::parseLong).orElse(null);
                var route = rcvMapper.map(source.getRoute());

                return PlanEntity.builder()
                        .planId(planId)
                        .title(title)
                        .startDate(startDate)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .ownerId(ownerId)
                        .version(version)
                        .route(route)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<EntityRouteComponent, RouteComponent> routeComponentViewMapper(
            final Mapper<WayPointEntity, WayPoint> wayPointViewMapper,
            final Mapper<DayEntity, Day> dayViewMapper
            ) {
        return new Mapper<>() {
            @Override
            protected RouteComponent mapInternal(EntityRouteComponent source) {
                if (source instanceof WayPointEntity o) {
                    return wayPointViewMapper.map(o);
                } else if (source instanceof DayEntity o) {
                    return dayViewMapper.map(o);
                } else {
                    throw new IllegalArgumentException("Unknown EntityRouteComponent type: " + source.getClass());
                }
            }
        };
    }

    @Bean
    public Mapper<RouteComponent,EntityRouteComponent> routeComponentDtoMapper(
            final Mapper<WayPoint, WayPointEntity> wayPointDtoMapper,
            final Mapper<Day, DayEntity> dayDtoMapper
            ) {
        return new Mapper<>() {
            @Override
            protected EntityRouteComponent mapInternal(RouteComponent source) {
                if (source instanceof WayPoint o) {
                    return wayPointDtoMapper.map(o);
                } else if (source instanceof Day o) {
                    return dayDtoMapper.map(o);
                } else {
                    throw new IllegalArgumentException("Unknown EntityRouteComponent type: " + source.getClass());
                }
            }
        };
    }

    @Bean
    public Mapper<WayPointEntity, WayPoint> wayPointViewMapper() {
        return new Mapper<>() {
            @Override
            protected WayPoint mapInternal(WayPointEntity source) {
                String wayPointId = source.getWayPointId() != null ? source.getWayPointId().toString() : null;
                String placeId = source.getPlaceId();
                String memo = source.getMemo();
                LocalTime time = source.getTime();
                return WayPoint.builder()
                        .waypointId(wayPointId)
                        .placeId(placeId)
                        .memo(memo)
                        .time(time)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<WayPoint, WayPointEntity> wayPointDtoMapper() {
        return new Mapper<>() {
            @Override
            protected WayPointEntity mapInternal(WayPoint source) {
                UUID wayPointId = UUID.fromString(source.waypointId());
                String placeId = source.placeId();
                String memo = source.memo();
                LocalTime time = source.time();
                return WayPointEntity.builder()
                        .wayPointId(wayPointId)
                        .placeId(placeId)
                        .memo(memo)
                        .time(time)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<DayEntity, Day> dayViewMapper(final Mapper<WayPointEntity, WayPoint> wayPointViewMapper) {
        return new Mapper<>() {
            @Override
            protected Day mapInternal(DayEntity source) {
                String dayId = source.getDayId() != null ? source.getDayId().toString() : null;
                List<WayPoint> wayPoints = wayPointViewMapper.map(source.getWayPoints());

                return Day.builder()
                        .dayId(dayId)
                        .wayPoints(wayPoints)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<Day,DayEntity> dayDtoMapper(final Mapper<WayPoint, WayPointEntity> wayPointDtoMapper) {
        return new Mapper<>() {
            @Override
            protected DayEntity mapInternal(Day source) {
                UUID dayId = UUID.fromString(source.dayId());
                List<WayPointEntity> wayPoints = wayPointDtoMapper.map(source.wayPoints());

                return DayEntity.builder()
                        .dayId(dayId)
                        .wayPoints(wayPoints)
                        .build();
            }
        };
    }

}
