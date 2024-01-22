package click.porito.travel_core.plan.api.reqeust.pointer;

import click.porito.travel_core.plan.PointedComponentNotFoundException;
import click.porito.travel_core.plan.domain.RouteComponent;

import java.util.List;

public sealed interface ComponentPointable<T extends RouteComponent> permits DayPointable, WayPointPointable {

    /**
     * 경로에서 해당 컴포넌트를 찾아 반환한다.
     *
     * @param route 경로
     * @return 컴포넌트
     * @throws PointedComponentNotFoundException 경로에서 해당 컴포넌트를 찾지 못한 경우
     */
    T getPointedComponent(List<RouteComponent> route) throws PointedComponentNotFoundException;
}

