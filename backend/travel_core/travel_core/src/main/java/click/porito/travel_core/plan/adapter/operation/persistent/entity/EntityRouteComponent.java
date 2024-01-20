package click.porito.travel_core.plan.adapter.operation.persistent.entity;

public sealed interface EntityRouteComponent permits WayPointEntity, DayEntity {
    public String getId();


}
