package click.porito.travel_core_service.plan.api.application.implementation;

import click.porito.plan_common.api.reqeust.ReorderRouteRequest;
import click.porito.plan_common.api.reqeust.pointer.StructureAwareDayPointer;
import click.porito.plan_common.api.reqeust.pointer.StructureAwarePointer;
import click.porito.plan_common.api.reqeust.pointer.StructureAwareWaypointPointer;
import click.porito.plan_common.api.response.RouteResponse;
import click.porito.plan_common.domain.Day;
import click.porito.plan_common.domain.Plan;
import click.porito.plan_common.domain.RouteComponent;
import click.porito.plan_common.domain.WayPoint;
import click.porito.travel_core_service.place.operation.application.PlaceOperation;
import click.porito.travel_core_service.plan.api.PlanRouteApiImpl;
import click.porito.travel_core_service.plan.operation.adapter.MockPlanOperation;
import click.porito.travel_core_service.plan.operation.application.PlanOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class PlanRouteApiImplTest {

    private PlanRouteApiImpl planRouteApi;
    private PlanOperation planOperation;
    private PlaceOperation placeOperation;
    private final String planId = "planId";

    @BeforeEach
    void setUp() {
        List<RouteComponent> route = new ArrayList<>();
        int dayCount = 0;
        int wayPointCount = 0;
        route.add(WayPoint.createWayPoint("way" + String.valueOf(wayPointCount++), UUID.randomUUID().toString()));
        for (int i = 0; i < 3; i++) {
            List<WayPoint> wayPoints = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                WayPoint tmp = WayPoint.createWayPoint("way" + String.valueOf(wayPointCount++), UUID.randomUUID().toString());
                wayPoints.add(tmp);
            }
            Day day = Day.builder()
                    .dayId("day" + String.valueOf(dayCount++))
                    .wayPoints(wayPoints)
                    .build();
            route.add(day);
        }
        // day id : 0 ~ 3
        // way point id : 0 ~ 10

        Plan plan = Plan.builder()
                .planId("planId")
                .route(route)
                .build();


        List<Plan> plans = new ArrayList<>();
        plans.add(plan);
        planOperation = spy(new MockPlanOperation(plans));
        placeOperation = mock(PlaceOperation.class);
        planRouteApi = new PlanRouteApiImpl(planOperation, placeOperation);
    }

    @Test
    void reorderRoute() {
        //given
        List<StructureAwarePointer> route = new ArrayList<>();
        int dayCount = 2;
        int wayPointCount = 9;
        route.add(new StructureAwareWaypointPointer("way" + String.valueOf(wayPointCount--)));
        for (int i = 0; i < 3; i++) {
            List<StructureAwareWaypointPointer> wayPoints = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                var tmp = new StructureAwareWaypointPointer("way" + String.valueOf(wayPointCount--));
                wayPoints.add(tmp);
            }
            var day = new StructureAwareDayPointer("day" + String.valueOf(dayCount--), wayPoints);
            route.add(day);
        }
        // 반대로 뒤집는 것을 테스트
        ReorderRouteRequest reorderRouteRequest = new ReorderRouteRequest(route);

        //when
        RouteResponse routeResponse = planRouteApi.reorderRoute(planId, reorderRouteRequest);

        //then
        List<RouteComponent> result = routeResponse.route();
        dayCount = 2;
        wayPointCount = 9;
        RouteComponent routeComponent = result.get(0);
        assertInstanceOf(WayPoint.class, routeComponent);
        WayPoint wayPoint = (WayPoint) routeComponent;
        assertEquals("way" + String.valueOf(wayPointCount--), wayPoint.getWaypointId());
        for (int i = 1; i < 4; i++) {
            routeComponent = result.get(i);
            assertInstanceOf(Day.class, routeComponent);
            Day day = (Day) routeComponent;
            assertEquals("day" + String.valueOf(dayCount--), day.getDayId());
            for (int j = 0; j < 3; j++) {
                routeComponent = day.getWayPoints().get(j);
                assertInstanceOf(WayPoint.class, routeComponent);
                wayPoint = (WayPoint) routeComponent;
                assertEquals("way" + String.valueOf(wayPointCount--), wayPoint.getWaypointId());
            }
        }

    }
}