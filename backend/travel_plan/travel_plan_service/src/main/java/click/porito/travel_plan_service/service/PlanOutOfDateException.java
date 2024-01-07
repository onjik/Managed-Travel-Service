package click.porito.travel_plan_service.service;

import click.porito.travel_plan_service.dto.PlanPutForm;

/**
 * {@link PlanService#putPlanInfo(String, PlanPutForm)} 에서 버전 정보가 일치하지 않을 때 발생하는 예외
 */
public class PlanOutOfDateException extends RuntimeException{
}
