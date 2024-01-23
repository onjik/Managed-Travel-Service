package click.porito.travel_core.plan.api.adapter.rest;

import click.porito.travel_core.plan.api.application.PlanRouteApi;
import click.porito.travel_core.plan.api.reqeust.ReorderRouteRequest;
import click.porito.travel_core.plan.api.reqeust.WayPointAppendAfterRequest;
import click.porito.travel_core.plan.api.reqeust.WayPointDetailUpdateRequest;
import click.porito.travel_core.plan.api.reqeust.pointer.DayPointable;
import click.porito.travel_core.plan.api.reqeust.pointer.WayPointPointable;
import click.porito.travel_core.plan.api.response.RouteResponse;
import click.porito.travel_core.plan.domain.Day;
import click.porito.travel_core.plan.domain.WayPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/plans/{planId}/route")
@RequiredArgsConstructor
public class PlanRouteRestController {
    private final PlanRouteApi planRouteApi;

    @GetMapping("/days/{dayId}")
    public ResponseEntity<Day> getDayPlan(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "dayId") String dayId
    ) {
        DayPointable dayPointable = new DayPointable(dayId);
        Day dayPlan = planRouteApi.getDayPlan(planId, dayPointable);
        return ResponseEntity.ok(dayPlan);
    }

    @PostMapping("/days")
    public ResponseEntity<RouteResponse> appendDayAfter(
            @PathVariable(name = "planId") String planId,
            @RequestBody(required = false) DayPointable dayPointable
    ) {
        RouteResponse routeResponse = planRouteApi.appendDayAfter(planId, dayPointable);
        return ResponseEntity.ok(routeResponse);
    }

    @DeleteMapping("/days/{dayId}")
    public ResponseEntity<RouteResponse> deleteDay(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "dayId") String dayId
    ) {
        DayPointable dayPointable = new DayPointable(dayId);
        RouteResponse routeResponse = planRouteApi.deleteDay(planId, dayPointable);
        return ResponseEntity.ok(routeResponse);
    }

    @GetMapping("/waypoints/{wayPointId}")
    public ResponseEntity<WayPoint> getWayPoint(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "wayPointId") String wayPointId
    ) {
        WayPointPointable wayPointPointable = new WayPointPointable(null, wayPointId);
        WayPoint wayPoint = planRouteApi.getWayPoint(planId, wayPointPointable);
        return ResponseEntity.ok(wayPoint);
    }

    @GetMapping("/days/{dayId}/waypoints/{wayPointId}")
    public ResponseEntity<WayPoint> getWayPoint(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "dayId") String dayId,
            @PathVariable(name = "wayPointId") String wayPointId
    ) {
        WayPointPointable wayPointPointable = new WayPointPointable(dayId, wayPointId);
        WayPoint wayPoint = planRouteApi.getWayPoint(planId, wayPointPointable);
        return ResponseEntity.ok(wayPoint);
    }


    @PatchMapping("/waypoints/{wayPointId}")
    public ResponseEntity<WayPoint> updateWayPoint(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "wayPointId") String wayPointId,
            @RequestBody WayPointDetailUpdateRequest request
    ) {
        WayPointPointable wayPointPointable = new WayPointPointable(null, wayPointId);
        WayPoint wayPoint = planRouteApi.updateWayPoint(planId, wayPointPointable, request);
        return ResponseEntity.ok(wayPoint);
    }

    @PatchMapping("/days/{dayId}/waypoints/{wayPointId}")
    public ResponseEntity<WayPoint> updateWayPoint(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "dayId") String dayId,
            @PathVariable(name = "wayPointId") String wayPointId,
            @RequestBody WayPointDetailUpdateRequest request
    ) {
        WayPointPointable wayPointPointable = new WayPointPointable(dayId, wayPointId);
        WayPoint wayPoint = planRouteApi.updateWayPoint(planId, wayPointPointable, request);
        return ResponseEntity.ok(wayPoint);
    }

    @PostMapping("/waypoints")
    public ResponseEntity<RouteResponse> appendWayPointAfter(
            @PathVariable(name = "planId") String planId,
            @RequestBody WayPointAppendAfterRequest request
            ) {
        RouteResponse routeResponse = planRouteApi.appendWayPointAfter(planId, request);
        return ResponseEntity.ok(routeResponse);
    }

    @DeleteMapping("/waypoints/{wayPointId}")
    public ResponseEntity<RouteResponse> deleteWayPoint(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "wayPointId") String wayPointId
    ) {
        WayPointPointable wayPointPointable = new WayPointPointable(null, wayPointId);
        RouteResponse routeResponse = planRouteApi.deleteWayPoint(planId, wayPointPointable);
        return ResponseEntity.ok(routeResponse);
    }

    @DeleteMapping("/days/{dayId}/waypoints/{wayPointId}")
    public ResponseEntity<RouteResponse> deleteWayPoint(
            @PathVariable(name = "planId") String planId,
            @PathVariable(name = "dayId") String dayId,
            @PathVariable(name = "wayPointId") String wayPointId
    ) {
        WayPointPointable wayPointPointable = new WayPointPointable(dayId, wayPointId);
        RouteResponse routeResponse = planRouteApi.deleteWayPoint(planId, wayPointPointable);
        return ResponseEntity.ok(routeResponse);
    }

    @PostMapping("/reorder")
    public ResponseEntity<RouteResponse> reorder(
            @PathVariable(name = "planId") String planId,
            @RequestBody ReorderRouteRequest reorderRouteRequest
            ) {
        RouteResponse reordered = planRouteApi.reorderRoute(planId, reorderRouteRequest);
        return ResponseEntity.ok(reordered);
    }


}
