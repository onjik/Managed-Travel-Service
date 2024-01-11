package click.porito.travel_core.plan.mapper;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.domain.RouteComponent;
import click.porito.travel_core.plan.domain.WayPoint;
import click.porito.travel_core.plan.dto.DayView;
import click.porito.travel_core.plan.dto.PlanView;
import click.porito.travel_core.plan.dto.RouteComponentView;
import click.porito.travel_core.plan.dto.WayPointView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
                    return null;
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


}
