package click.porito.plan_connector;

import click.porito.common.exception.Domain;
import click.porito.connector.AbstractRestConnector;
import click.porito.plan_common.api.PlanApi;
import click.porito.plan_common.api.PlanRouteApi;
import click.porito.plan_common.api.reqeust.*;
import click.porito.plan_common.api.reqeust.pointer.DayPointable;
import click.porito.plan_common.api.reqeust.pointer.WayPointPointable;
import click.porito.plan_common.api.response.RouteResponse;
import click.porito.plan_common.domain.Day;
import click.porito.plan_common.domain.Plan;
import click.porito.plan_common.domain.WayPoint;
import click.porito.plan_common.exception.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

public class PlanRestConnector extends AbstractRestConnector implements PlanApi, PlanRouteApi {
    public PlanRestConnector(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public Optional<Plan> getPlan(String planId) {
        return doExchange(
                restTemplate -> restTemplate.getForEntity(
                        "/v1/plans/{planId}",
                        Plan.class,
                        planId
                )
        );
    }

    @Override
    public Plan createPlan(String userId, PlanCreateRequest planCreateRequest) {
        return doExchange(
                restTemplate -> restTemplate.postForEntity(
                        "/v1/plans",
                        planCreateRequest,
                        Plan.class
                )
        ).orElse(null);
    }

    @Override
    public List<Plan> getPlansOwnedBy(String ownerId, Pageable pageable) {
        return doExchange(
                restTemplate -> restTemplate.exchange(
                        "/v1/plans?ownerId={ownerId}&page={page}&size={size}",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Plan>>(){},
                        ownerId, pageable.getPageNumber(), pageable.getPageSize()
                )
        ).orElse(null);
    }

    @Override
    public void deletePlan(String planId) {
        doRequest(
                restTemplate -> restTemplate.delete(
                        "/v1/plans/{planId}",
                        planId
                )
        );
    }

    @Override
    public Plan updatePlan(String planId, PlanUpdateRequest planUpdateRequest) throws InvalidUpdateInfoException, PlanVersionOutOfDateException {
        return doExchange(
                restTemplate -> restTemplate.exchange(
                        "/v1/plans/{planId}",
                        HttpMethod.PUT,
                        new HttpEntity<>(planUpdateRequest),
                        Plan.class,
                        planId
                )
        ).orElse(null);
    }

    @Override
    protected Domain getDomain() {
        return Domain.PLAN;
    }

    @Override
    public Day getDayPlan(String planId, DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        return doExchange(
                restTemplate -> restTemplate.getForEntity(
                        "/v1/plans/{planId}/route/days/{dayId}",
                        Day.class,
                        planId, dayPointer.dayId()
                )
        ).orElse(null);
    }

    @Override
    public RouteResponse appendDayAfter(String planId, DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        return doExchange(
                restTemplate -> restTemplate.postForEntity(
                        "/v1/plans/{planId}/route/days",
                        dayPointer,
                        RouteResponse.class,
                        planId
                )
        ).orElse(null);
    }

    @Override
    public RouteResponse deleteDay(String planId, DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        return doExchange(
                restTemplate -> restTemplate.exchange(
                        "/v1/plans/{planId}/route/days/{dayId}",
                        HttpMethod.DELETE,
                        null,
                        RouteResponse.class,
                        planId, dayPointer.dayId()
                )
        ).orElse(null);
    }

    @Override
    public WayPoint getWayPoint(String planId, WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        if (wayPointPointer.dayId() != null){
            return doExchange(
                    restTemplate -> restTemplate.getForEntity(
                            "/v1/plans/{planId}/route/days/{dayId}/waypoints/{wayPointId}",
                            WayPoint.class,
                            planId, wayPointPointer.dayId(), wayPointPointer.wayPointId()
                    )
            ).orElse(null);
        } else {
            return doExchange(
                    restTemplate -> restTemplate.getForEntity(
                            "/v1/plans/{planId}/route/waypoints/{wayPointId}",
                            WayPoint.class,
                            planId, wayPointPointer.wayPointId()
                    )
            ).orElse(null);
        }
    }

    @Override
    public WayPoint updateWayPoint(String planId, WayPointPointable wayPointPointer, WayPointDetailUpdateRequest request) throws PlanNotFoundException, PointedComponentNotFoundException {
        if (wayPointPointer.dayId() != null) {
            return doExchange(
                    restTemplate -> restTemplate.exchange(
                            "/v1/plans/{planId}/route/days/{dayId}/waypoints/{wayPointId}",
                            HttpMethod.PATCH,
                            new HttpEntity<>(request),
                            WayPoint.class,
                            planId, wayPointPointer.dayId(), wayPointPointer.wayPointId()
                    )
            ).orElse(null);
        } else {
            return doExchange(
                    restTemplate -> restTemplate.exchange(
                            "/v1/plans/{planId}/route/waypoints/{wayPointId}",
                            HttpMethod.PATCH,
                            new HttpEntity<>(request),
                            WayPoint.class,
                            planId, wayPointPointer.wayPointId()
                    )
            ).orElse(null);
        }
    }

    @Override
    public RouteResponse appendWayPointAfter(String planId, WayPointAppendAfterRequest request) throws PlanNotFoundException, PointedComponentNotFoundException, InvalidRouteReorderRequestException {
        return doExchange(
                restTemplate -> restTemplate.postForEntity(
                        "/v1/plans/{planId}/route/waypoints",
                        request,
                        RouteResponse.class,
                        planId
                )
        ).orElse(null);
    }

    @Override
    public RouteResponse deleteWayPoint(String planId, WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException {
        if (wayPointPointer.dayId() != null) {
            return doExchange(
                    restTemplate -> restTemplate.exchange(
                            "/v1/plans/{planId}/route/days/{dayId}/waypoints/{wayPointId}",
                            HttpMethod.DELETE,
                            null,
                            RouteResponse.class,
                            planId, wayPointPointer.dayId(), wayPointPointer.wayPointId()
                    )
            ).orElse(null);
        } else {
            return doExchange(
                    restTemplate -> restTemplate.exchange(
                            "/v1/plans/{planId}/route/waypoints/{wayPointId}",
                            HttpMethod.DELETE,
                            null,
                            RouteResponse.class,
                            planId, wayPointPointer.wayPointId()
                    )
            ).orElse(null);
        }
    }

    @Override
    public RouteResponse reorderRoute(String planId, ReorderRouteRequest request) throws PointedComponentNotFoundException, PlanNotFoundException, InvalidRouteReorderRequestException {
        return doExchange(
                restTemplate -> restTemplate.postForEntity(
                        "/v1/plans/{planId}/route/reorder",
                        request,
                        RouteResponse.class,
                        planId
                )
        ).orElse(null);
    }
}