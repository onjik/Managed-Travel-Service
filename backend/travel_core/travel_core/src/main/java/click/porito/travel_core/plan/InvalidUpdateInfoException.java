package click.porito.travel_core.plan;

import click.porito.travel_core.plan.PlanService;
import click.porito.travel_core.plan.dto.PlanPutForm;

/**
 * {@link PlanService#putPlanInfo(String, PlanPutForm)} 에서 잘못된 정보가 전달되었을 때 발생하는 예외
 */
public class InvalidUpdateInfoException extends RuntimeException {

}