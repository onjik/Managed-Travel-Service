package click.porito.managed_travel.plan.plan_connector;

import click.porito.common.exception.Domain;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.AbstractRestConnector;
import click.porito.managed_travel.common.porito_travel_spring_boot_starter_servlet_connector.RestExchangeable;
import click.porito.managed_travel.plan.domain.api.PlanApi;
import click.porito.managed_travel.plan.domain.api.PlanRouteApi;
import click.porito.managed_travel.plan.domain.api.reqeust.*;
import click.porito.managed_travel.plan.domain.exception.*;
import click.porito.managed_travel.plan.exception.*;
import click.porito.managed_travel.plan.domain.api.reqeust.pointer.DayPointable;
import click.porito.managed_travel.plan.domain.api.reqeust.pointer.WayPointPointable;
import click.porito.managed_travel.plan.domain.api.response.RouteResponse;
import click.porito.managed_travel.plan.Day;
import click.porito.managed_travel.plan.Plan;
import click.porito.managed_travel.plan.WayPoint;
import click.porito.plan_common.exception.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PlanRestConnector extends AbstractRestConnector implements PlanApi, PlanRouteApi {

    public PlanRestConnector(RestExchangeable restExchangeable, String uriPrefix) {
        super(restExchangeable, uriPrefix);
    }

    @Override
    public Optional<Plan> getPlan(String planId) {
        return doExchange("/v1/plans/{planId}", HttpMethod.GET, null, Plan.class, planId);
    }

    @Override
    public Plan createPlan(String userId, PlanCreateRequest planCreateRequest) {
        return doExchange("/v1/plans", HttpMethod.POST, new HttpEntity<>(planCreateRequest), Plan.class).orElse(null);
    }

    @Override
    public List<Plan> getPlansOwnedBy(String ownerId, Pageable pageable) {
        return doExchange(
                "/v1/plans?ownerId={ownerId}&page={page}&size={size}",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Plan>>(){}, ownerId, pageable.getPageNumber(), pageable.getPageSize())
                .orElse(Collections.emptyList());
    }

    @Override
    public void deletePlan(String planId) {
        doRequest("/v1/plans/{planId}", HttpMethod.DELETE, null, Void.class, planId);
    }

    @Override
    public Plan updatePlan(String planId, PlanUpdateRequest planUpdateRequest) throws InvalidUpdateInfoException, PlanVersionOutOfDateException {
        return doExchange(
                "/v1/plans/{planId}",
                HttpMethod.PUT,
                new HttpEntity<>(planUpdateRequest),
                Plan.class,
                planId
        ).orElse(null);
    }

    @Override
    protected Domain getDomain() {
        return Domain.PLAN;
    }

    @Override
    public Day getDayPlan(String planId, DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        return doExchange(
                "/v1/plans/{planId}/route/days/{dayId}",
                HttpMethod.GET,
                null,
                Day.class,
                planId, dayPointer.dayId()
        ).orElse(null);
    }

    @Override
    public RouteResponse appendDayAfter(String planId, DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        return doExchange(
                "/v1/plans/{planId}/route/days",
                HttpMethod.POST,
                new HttpEntity<>(dayPointer),
                RouteResponse.class,
                planId
        ).orElse(null);
    }

    @Override
    public RouteResponse deleteDay(String planId, DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        return doExchange(
                "/v1/plans/{planId}/route/days/{dayId}",
                HttpMethod.DELETE,
                null,
                RouteResponse.class,
                planId, dayPointer.dayId()
        ).orElse(null);
    }

    @Override
    public WayPoint getWayPoint(String planId, WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        if (wayPointPointer.dayId() != null){
            return doExchange(
                    "/v1/plans/{planId}/route/days/{dayId}/waypoints/{wayPointId}",
                    HttpMethod.GET,
                    null,
                    WayPoint.class,
                    planId, wayPointPointer.dayId(), wayPointPointer.wayPointId()
            ).orElse(null);
        } else {
            return doExchange(
                    "/v1/plans/{planId}/route/waypoints/{wayPointId}",
                    HttpMethod.GET,
                    null,
                    WayPoint.class,
                    planId, wayPointPointer.wayPointId()
            ).orElse(null);
        }
    }

    @Override
    public WayPoint updateWayPoint(String planId, WayPointPointable wayPointPointer, WayPointDetailUpdateRequest request) throws PlanNotFoundException, PointedComponentNotFoundException {
        if (wayPointPointer.dayId() != null) {
            return doExchange(
                    "/v1/plans/{planId}/route/days/{dayId}/waypoints/{wayPointId}",
                    HttpMethod.PATCH,
                    new HttpEntity<>(request),
                    WayPoint.class,
                    planId, wayPointPointer.dayId(), wayPointPointer.wayPointId()
            ).orElse(null);
        } else {
            return doExchange(
                    "/v1/plans/{planId}/route/waypoints/{wayPointId}",
                    HttpMethod.PATCH,
                    new HttpEntity<>(request),
                    WayPoint.class,
                    planId, wayPointPointer.wayPointId()
            ).orElse(null);
        }
    }

    @Override
    public RouteResponse appendWayPointAfter(String planId, WayPointAppendAfterRequest request) throws PlanNotFoundException, PointedComponentNotFoundException, InvalidRouteReorderRequestException {
        return doExchange(
                "/v1/plans/{planId}/route/waypoints",
                HttpMethod.POST,
                new HttpEntity<>(request),
                RouteResponse.class,
                planId
        ).orElse(null);
    }

    @Override
    public RouteResponse deleteWayPoint(String planId, WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        if (wayPointPointer.dayId() != null) {
            return doExchange(
                    "/v1/plans/{planId}/route/days/{dayId}/waypoints/{wayPointId}",
                    HttpMethod.DELETE,
                    null,
                    RouteResponse.class,
                    planId, wayPointPointer.dayId(), wayPointPointer.wayPointId()
            ).orElse(null);
        } else {
            return doExchange(
                    "/v1/plans/{planId}/route/waypoints/{wayPointId}",
                    HttpMethod.DELETE,
                    null,
                    RouteResponse.class,
                    planId, wayPointPointer.wayPointId()
            ).orElse(null);
        }
    }

    @Override
    public RouteResponse reorderRoute(String planId, ReorderRouteRequest request) throws PointedComponentNotFoundException, PlanNotFoundException, InvalidRouteReorderRequestException {
        return doExchange(
                "/v1/plans/{planId}/route/reorder",
                HttpMethod.POST,
                new HttpEntity<>(request),
                RouteResponse.class,
                planId
        ).orElse(null);
    }
}
