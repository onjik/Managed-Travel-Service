package click.porito.managed_travel.plan.domain.api.reqeust.pointer;

import click.porito.managed_travel.plan.Day;
import click.porito.managed_travel.plan.RouteComponent;
import click.porito.managed_travel.plan.domain.exception.PointedComponentNotFoundException;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DayPointable(
        @NotBlank
        String dayId
) implements ComponentPointable<Day> {
    @Override
    public Day getPointedComponent(List<RouteComponent> route) throws PointedComponentNotFoundException {
        // 여행 Day 의 경우 여행에 직접 속하므로,
        return route.stream()
                .filter(Day.class::isInstance)
                .map(Day.class::cast)
                .filter(day -> this.dayId.equals(day.getDayId()))
                .findFirst()
                .orElseThrow(() -> new PointedComponentNotFoundException(this));
    }

}
