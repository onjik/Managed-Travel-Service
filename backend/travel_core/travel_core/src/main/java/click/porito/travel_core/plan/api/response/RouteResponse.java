package click.porito.travel_core.plan.api.response;

import click.porito.travel_core.plan.domain.Plan;
import click.porito.travel_core.plan.domain.RouteComponent;

import java.util.List;

/**
 * 경로를 표현하는 일급 객체
 */
public record RouteResponse(
        List<RouteComponent> route
) {
    public static RouteResponse of(List<RouteComponent> route) {
        return new RouteResponse(route);
    }

    public static RouteResponse from(Plan plan) {
        return new RouteResponse(plan.getRoute());
    }
}
