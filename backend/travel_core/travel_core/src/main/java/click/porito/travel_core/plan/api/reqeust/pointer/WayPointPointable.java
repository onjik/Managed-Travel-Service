package click.porito.travel_core.plan.api.reqeust.pointer;

import click.porito.travel_core.plan.PointedComponentNotFoundException;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.RouteComponent;
import click.porito.travel_core.plan.domain.WayPoint;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record WayPointPointable(
        @Nullable
        String dayId,
        @NotBlank
        String wayPointId
) implements ComponentPointable<WayPoint> {
        @Override
        public WayPoint getPointedComponent(List<RouteComponent> route) throws PointedComponentNotFoundException {
                // 여행의 경우, 여행에 직접 속할 수도, Day 에 속할 수도 있다.
                final List<WayPoint> waypoints;
                if (dayId == null) {
                        // dayId 가 없는 경우, 여행에 직접 속한 Waypoint 에서 찾는다.
                        waypoints = route.stream()
                                .filter(WayPoint.class::isInstance)
                                .map(WayPoint.class::cast)
                                .toList();
                } else {
                        // dayId 가 있는 경우, 그 Day 에 속한 Waypoint 에서 찾는다.
                        waypoints = route.stream()
                                .filter(Day.class::isInstance)
                                .map(Day.class::cast)
                                .filter(day -> this.dayId.equals(day.getDayId()))
                                .flatMap(day -> day.getWayPoints().stream())
                                .toList();
                }

                return waypoints.stream()
                        .filter(wayPoint -> this.wayPointId.equals(wayPoint.getWaypointId()))
                        .findFirst()
                        .orElseThrow(() -> new PointedComponentNotFoundException(this));
        }


}
