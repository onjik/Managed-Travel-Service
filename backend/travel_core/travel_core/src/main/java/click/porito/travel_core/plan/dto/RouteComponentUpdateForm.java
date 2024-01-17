package click.porito.travel_core.plan.dto;

import org.springframework.lang.Nullable;

public sealed interface RouteComponentUpdateForm permits DayUpdateForm, WayPointUpdateForm {
    @Nullable
    String id();
}
