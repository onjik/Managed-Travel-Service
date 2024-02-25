package click.porito.managed_travel.plan.plan_service.operation.adapter.persistent.entity;

public sealed interface EntityRouteComponent permits WayPointEntity, DayEntity {
    public String getId();


}
