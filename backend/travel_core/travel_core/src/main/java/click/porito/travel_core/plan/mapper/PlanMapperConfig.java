package click.porito.travel_core.plan.mapper;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.plan.api.rest.DayUpdateRequest;
import click.porito.travel_core.plan.api.rest.RouteComponentRequest;
import click.porito.travel_core.plan.api.rest.WayPointUpdateRequest;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.domain.RouteComponent;
import click.porito.travel_core.plan.domain.WayPoint;
import click.porito.travel_core.plan.dto.*;
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
    public Mapper<Plan, PlanView> planViewMapper(final Mapper<RouteComponent, RouteComponentView> rcvMapper) {
        return new Mapper<>() {
            @Override
            protected PlanView mapInternal(Plan source) {
                String planId = source.getPlanId();
                String title = source.getTitle();
                LocalDate startDate = source.getStartDate(); // nullable
                Instant createdAt = source.getCreatedAt();
                Instant updatedAt = source.getUpdatedAt();
                String version = Optional.ofNullable(source.getVersion())
                        .map(Object::toString).orElse(null);
                var route = rcvMapper.map(source.getRoute());

                return PlanView.builder()
                        .planId(planId)
                        .title(title)
                        .startDate(startDate)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .version(version)
                        .route(route)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<RouteComponent, RouteComponentView> routeComponentViewMapper(
            final Mapper<WayPoint, WayPointView> wayPointViewMapper,
            final Mapper<Day, DayView> dayViewMapper
            ) {
        return new Mapper<>() {
            @Override
            protected RouteComponentView mapInternal(RouteComponent source) {
                if (source instanceof WayPoint o) {
                    return wayPointViewMapper.map(o);
                } else if (source instanceof Day o) {
                    return dayViewMapper.map(o);
                } else {
                    throw new IllegalArgumentException("Unknown RouteComponent type: " + source.getClass());
                }
            }
        };
    }

    @Bean
    public Mapper<WayPoint, WayPointView> wayPointViewMapper() {
        return new Mapper<>() {
            @Override
            protected WayPointView mapInternal(WayPoint source) {
                String wayPointId = source.getWayPointId() != null ? source.getWayPointId().toString() : null;
                String placeId = source.getPlaceId();
                String memo = source.getMemo();
                LocalTime time = source.getTime();
                return WayPointView.builder()
                        .waypointId(wayPointId)
                        .placeId(placeId)
                        .memo(memo)
                        .time(time)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<Day, DayView> dayViewMapper(final Mapper<WayPoint, WayPointView> wayPointViewMapper) {
        return new Mapper<>() {
            @Override
            protected DayView mapInternal(Day source) {
                String dayId = source.getDayId() != null ? source.getDayId().toString() : null;
                List<WayPointView> wayPointViews = wayPointViewMapper.map(source.getWayPoints());

                return DayView.builder()
                        .dayId(dayId)
                        .wayPoints(wayPointViews)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<RouteComponentRequest, RouteComponentUpdateForm> routeComponentPutFormMapper(
            final Mapper<DayUpdateRequest, DayUpdateForm> dayMapper,
            final Mapper<WayPointUpdateRequest, WayPointUpdateForm> wayPointMapper) {
        return new Mapper<>() {
            @Override
            protected RouteComponentUpdateForm mapInternal(RouteComponentRequest source) {
                if (source instanceof DayUpdateRequest o){
                    return dayMapper.map(o);
                } else if (source instanceof WayPointUpdateRequest o){
                    return wayPointMapper.map(o);
                } else {
                    throw new IllegalArgumentException("Unknown RouteComponent type: " + source.getClass());
                }
            }
        };
    }

    @Bean
    public Mapper<DayUpdateRequest, DayUpdateForm> dayPutFormMapper(
            final Mapper<WayPointUpdateRequest, WayPointUpdateForm> wayPointMapper) {
        return new Mapper<>() {
            @Override
            protected DayUpdateForm mapInternal(DayUpdateRequest source) {
                String dayId = source.dayId();
                List<WayPointUpdateForm> wayPoints = wayPointMapper.map(source.wayPoints());
                return new DayUpdateForm(dayId, wayPoints);
            }
        };
    }

    @Bean
    public Mapper<WayPointUpdateRequest, WayPointUpdateForm> wayPointPutFormMapper() {
        return new Mapper<>() {
            @Override
            protected WayPointUpdateForm mapInternal(WayPointUpdateRequest source) {
                String wayPointId = source.wayPointId();
                String placeId = source.placeId();
                String memo = source.memo();
                LocalTime time = source.time();
                return new WayPointUpdateForm(wayPointId, placeId, memo, time);
            }
        };
    }

    @Bean
    public Mapper<RouteComponentUpdateForm, RouteComponent> routeComponentMapper(
            final Mapper<DayUpdateForm, Day> dayMapper,
            final Mapper<WayPointUpdateForm, WayPoint> wayPointMapper) {
        return new Mapper<>() {
            @Override
            protected RouteComponent mapInternal(RouteComponentUpdateForm source) {
                if (source instanceof DayUpdateForm o){
                    return dayMapper.map(o);
                } else if (source instanceof WayPointUpdateForm o){
                    return wayPointMapper.map(o);
                } else {
                    throw new IllegalArgumentException("Unknown RouteComponent type: " + source.getClass());
                }
            }
        };
    }

    @Bean
    public Mapper<DayUpdateForm, Day> dayMapper(final Mapper<WayPointUpdateForm, WayPoint> wayPointMapper) {
        return new Mapper<>() {
            @Override
            protected Day mapInternal(DayUpdateForm source) {
                final String givenDayId = source.dayId();
                final UUID dayId = givenDayId != null ? UUID.fromString(givenDayId) : UUID.randomUUID();
                final List<WayPoint> wayPoints = wayPointMapper.map(source.wayPoints());
                Day day = new Day(dayId);
                day.setWayPoints(wayPoints);
                return day;
            }
        };
    }

    @Bean
    public Mapper<WayPointUpdateForm, WayPoint> wayPointMapper() {
        return new Mapper<>() {
            @Override
            protected WayPoint mapInternal(WayPointUpdateForm source) {
                final String givenWayPointId = source.wayPointId();
                final UUID wayPointId = givenWayPointId != null ? UUID.fromString(givenWayPointId) : UUID.randomUUID();
                final String placeId = source.placeId();
                final String memo = source.memo();
                final LocalTime time = source.time();
                WayPoint wayPoint = new WayPoint(wayPointId, placeId);
                wayPoint.setMemo(memo);
                wayPoint.setTime(time);
                return wayPoint;
            }
        };
    }

}
