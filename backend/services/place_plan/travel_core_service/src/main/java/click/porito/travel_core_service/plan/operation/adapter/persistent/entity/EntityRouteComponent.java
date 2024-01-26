package click.porito.travel_core_service.plan.operation.adapter.persistent.entity;

public sealed interface EntityRouteComponent permits WayPointEntity, DayEntity {
    public String getId();


}
