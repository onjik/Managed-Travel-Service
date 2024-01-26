package click.porito.travel_core_service.plan.api;

import click.porito.common.exception.FieldError;
import click.porito.plan_common.api.PlanRouteApi;
import click.porito.plan_common.api.reqeust.ReorderRouteRequest;
import click.porito.plan_common.api.reqeust.WayPointAppendAfterRequest;
import click.porito.plan_common.api.reqeust.WayPointDetailUpdateRequest;
import click.porito.plan_common.api.reqeust.pointer.*;
import click.porito.plan_common.api.response.RouteResponse;
import click.porito.plan_common.domain.Day;
import click.porito.plan_common.domain.Plan;
import click.porito.plan_common.domain.RouteComponent;
import click.porito.plan_common.domain.WayPoint;
import click.porito.plan_common.exception.InvalidRouteReorderRequestException;
import click.porito.plan_common.exception.InvalidUpdateInfoException;
import click.porito.plan_common.exception.PlanNotFoundException;
import click.porito.plan_common.exception.PointedComponentNotFoundException;
import click.porito.travel_core_service.place.operation.application.PlaceOperation;
import click.porito.travel_core_service.plan.operation.application.PlanOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Service
@RequiredArgsConstructor
public class PlanRouteApiImpl implements PlanRouteApi {
    private final PlanOperation planOperation;
    private final PlaceOperation placeOperation;

    @Override
    @PreAuthorize("@planAccessPolicy.canRead(authentication, #planId)")
    public Day getDayPlan(String planId, @Valid DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        Assert.notNull(planId, "planId must not be null");
        List<RouteComponent> route = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId))
                .getRoute();
        return dayPointer.getPointedComponent(route);
    }

    @Override
    //@Transactional
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public RouteResponse appendDayAfter(String planId, @Nullable @Valid DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        Assert.notNull(planId, "planId must not be null");
        Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        var route = plan.getRoute();

        if (dayPointer == null) {
            //맨 앞에 추가
            route.add(0, Day.createEmptyDay());
        } else {
            var day = dayPointer.getPointedComponent(route);
            var index = route.indexOf(day);
            route.add(index + 1, Day.createEmptyDay());
        }
        Plan updatedPlan = planOperation.update(plan);
        return RouteResponse.from(updatedPlan);
    }

    @Override
    //@Transactional
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public RouteResponse deleteDay(String planId, @Valid DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(dayPointer, "dayPointer must not be null");
        Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        var route = plan.getRoute();

        var day = dayPointer.getPointedComponent(route);
        boolean remove = route.remove(day);
        if (!remove) {
            throw new PointedComponentNotFoundException(dayPointer);
        }

        Plan updatedPlan = planOperation.update(plan);
        return RouteResponse.from(updatedPlan);
    }

    @Override
    @PreAuthorize("@planAccessPolicy.canRead(authentication, #planId)")
    public WayPoint getWayPoint(String planId, @Valid WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(wayPointPointer, "wayPointPointer must not be null");
        List<RouteComponent> route = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId))
                .getRoute();
        return wayPointPointer.getPointedComponent(route);
    }

    @Override
    //@Transactional
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public WayPoint updateWayPoint(String planId, @Valid WayPointPointable wayPointPointer, @Valid WayPointDetailUpdateRequest request) throws PlanNotFoundException, PointedComponentNotFoundException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(wayPointPointer, "wayPointPointer must not be null");
        Assert.notNull(request, "request must not be null");

        Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        var route = plan.getRoute();

        var wayPoint = wayPointPointer.getPointedComponent(route);

        //parse request
        String placeId = request.placeId();
        String memo = request.memo();
        LocalTime time = request.time();

        //update
        if (placeId != null) {
            wayPoint.setPlaceId(placeId);
        }
        if (memo != null) {
            wayPoint.setMemo(memo);
        }
        if (time != null) {
            wayPoint.setTime(time);
        }

        Plan updatedPlan = planOperation.update(plan);
        return wayPointPointer.getPointedComponent(updatedPlan.getRoute());
    }

    @Override
    //@Transactional
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public RouteResponse appendWayPointAfter(String planId, @Valid WayPointAppendAfterRequest request) throws PlanNotFoundException, PointedComponentNotFoundException, InvalidUpdateInfoException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(request, "request must not be null");
        String placeId = request.placeId();
        String dayId = request.dayId(); // nullable
        String wayPointId = request.wayPointId(); // nullable
        //find plan
        Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        //placeId 가 유효한지 확인
        boolean exists = placeOperation.exists(placeId);
        if (!exists) {
            List<FieldError> fieldErrors = FieldError.of("placeId", placeId, "invalid placeId");
            throw new InvalidUpdateInfoException(fieldErrors);
        }

        //create waypoint
        WayPoint wayPoint = WayPoint.createWayPoint(placeId);

        //경우에 따라 분기
        if (wayPointId != null){ // 특정 waypoint 뒤에 추가하라는 소리
            appendAfterWayPoint(plan, wayPoint, dayId, wayPointId);
        } else { // 맨 앞에 추가하라는 소리
            appendWayPointAtFirst(plan, wayPoint, dayId);
        }
        Plan updatedPlan = planOperation.update(plan);
        return RouteResponse.from(updatedPlan);
    }


    /**
     * @param plan plan
     * @param element 추가할 element
     * @param dayId dayId, null 이면 여행에 직접 속한 waypoint 뒤에 추가한다.
     * @param wayPointId wayPointId, never null
     * @throws PointedComponentNotFoundException dayId 가 null 이 아닌데 해당 dayId 를 가진 day 가 없을 때
     */
    private void appendAfterWayPoint(Plan plan, WayPoint element, @Nullable String dayId, @NonNull String wayPointId) throws PointedComponentNotFoundException, PlanNotFoundException, InvalidRouteReorderRequestException {
        var route = plan.getRoute();
        if (dayId != null) {
            List<WayPoint> wayPoints = route.stream()
                    .filter(Day.class::isInstance)
                    .map(Day.class::cast)
                    .filter(day -> dayId.equals(day.getDayId()))
                    .findFirst()
                    .orElseThrow(PointedComponentNotFoundException::new)
                    .getWayPoints();
            // wayPointId 가 가리키는 wayPoint 뒤에 추가한다.
            int index = wayPoints.stream()
                    .filter(wayPoint -> wayPointId.equals(wayPoint.getWaypointId()))
                    .findFirst()
                    .map(wayPoints::indexOf)
                    .orElseThrow(PointedComponentNotFoundException::new);
            wayPoints.add(index + 1, element);
        } else {
            // waypointId 가 가리키는 waypoint 뒤에 추가한다.
            int index = route.stream()
                    .filter(WayPoint.class::isInstance)
                    .map(WayPoint.class::cast)
                    .filter(wayPoint -> wayPointId.equals(wayPoint.getWaypointId()))
                    .findFirst()
                    .map(route::indexOf)
                    .filter(i -> i != -1)
                    .orElseThrow(PointedComponentNotFoundException::new);
            route.add(index + 1, element);
        }
    }

    private void appendWayPointAtFirst(Plan plan, WayPoint element, @Nullable String dayId){
        if (dayId != null) {
            List<WayPoint> wayPoints = plan.getRoute().stream()
                    .filter(Day.class::isInstance)
                    .map(Day.class::cast)
                    .filter(day -> dayId.equals(day.getDayId()))
                    .findFirst()
                    .orElseThrow(PointedComponentNotFoundException::new)
                    .getWayPoints();
            wayPoints.add(0, element);
        } else {
            plan.getRoute().add(0, element);
        }
    }


    @Override
    //@Transactional
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public RouteResponse deleteWayPoint(String planId, @Valid WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(wayPointPointer, "wayPointPointer must not be null");
        Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        var route = plan.getRoute();
        WayPoint pointedComponent = wayPointPointer.getPointedComponent(route);
        boolean remove = route.remove(pointedComponent);
        if (!remove) {
            throw new PointedComponentNotFoundException(wayPointPointer);
        }
        Plan updatedPlan = planOperation.update(plan);
        return RouteResponse.from(updatedPlan);
    }

    @Override
    //@Transactional
    @PreAuthorize("@planAccessPolicy.canUpdate(authentication, #planId)")
    public RouteResponse reorderRoute(String planId, @Valid ReorderRouteRequest request) throws PlanNotFoundException, InvalidRouteReorderRequestException {
        Assert.notNull(planId, "planId must not be null");
        Assert.notNull(request, "request must not be null");
        List<StructureAwarePointer> requestedRoute = request.route();
        final Plan plan = planOperation.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        List<RouteComponent> route = plan.getRoute();

        //순서대로 정렬
        Map<String, WayPoint> wayPointMap = new HashMap<>();
        Map<String, Day> dayMap = new HashMap<>();
        for (var component : route) {
            if (component instanceof Day day) {
                for (var wayPoint : day.getWayPoints()) {
                    wayPointMap.put(wayPoint.getWaypointId(), wayPoint);
                }
                //clear
                day.getWayPoints().clear();
                dayMap.put(day.getDayId(), day);
            } else if (component instanceof WayPoint wayPoint) {
                wayPointMap.put(wayPoint.getWaypointId(), wayPoint);
            } else {
                throw new IllegalStateException("invalid route component");
            }
        }

        final List<RouteComponent> reorderedRoute = new ArrayList<>();
        for (var component : requestedRoute) {
            if (component instanceof StructureAwareDayPointer day) {
                //날짜를 구성한다
                List<WayPoint> dayRoute = new ArrayList<>();
                if (day.waypoints() != null) {
                    for (var wayPoint : day.waypoints()) {
                        WayPoint removed = wayPointMap.remove(wayPoint.waypointId());
                        if (removed == null) {
                            throw new InvalidRouteReorderRequestException();
                        }
                        dayRoute.add(removed);
                    }
                }
                Day theDay = dayMap.remove(day.dayId());
                if (theDay == null) {
                    throw new InvalidRouteReorderRequestException();
                }
                theDay.setWayPoints(dayRoute);
                //구성한 날짜를 추가한다
                reorderedRoute.add(theDay);
            } else if (component instanceof StructureAwareWaypointPointer wayPoint) {
                //웨이포인트를 구성한다
                WayPoint removed = wayPointMap.remove(wayPoint.waypointId());
                if (removed == null) {
                    throw new InvalidRouteReorderRequestException();
                }
                //구성한 웨이포인트를 추가한다
                reorderedRoute.add(removed);
            } else {
                throw new IllegalStateException("invalid StructureAwarePointer");
            }
        }

        //남은게 있으면, 잘못된 요청이다.
        if (!wayPointMap.isEmpty()) {
            throw new InvalidRouteReorderRequestException();
        }

        //순서대로 정렬한 것을 route 에 반영한다.
        plan.setRoute(reorderedRoute);
        Plan updatedPlan = planOperation.update(plan);

        return RouteResponse.from(updatedPlan);
    }

}
