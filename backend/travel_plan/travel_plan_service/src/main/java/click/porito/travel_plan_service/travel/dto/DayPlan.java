package click.porito.travel_plan_service.travel.dto;

import java.time.LocalDate;
import java.util.List;

public class DayPlan {
    private String id;
    private LocalDate date;
    private List<WayPoint> wayPoints;
    private String accommodationId;

}
