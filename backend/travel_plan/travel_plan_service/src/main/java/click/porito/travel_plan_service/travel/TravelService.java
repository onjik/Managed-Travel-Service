package click.porito.travel_plan_service.travel;

import click.porito.travel_plan_service.travel.dto.Plan;
import click.porito.travel_plan_service.travel.dto.TravelView;

public interface TravelService {
    TravelView createPlan(String title);
    TravelView createPlan(Plan plan);
    TravelView updatePlan(Long id, Plan plan);
    TravelView getPlan(Long id);
    void deletePlan(Long id);
}
