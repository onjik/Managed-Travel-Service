package click.porito.managed_travel.plan.domain.api;

import click.porito.managed_travel.plan.domain.api.reqeust.ReorderRouteRequest;
import click.porito.managed_travel.plan.domain.api.reqeust.WayPointAppendAfterRequest;
import click.porito.managed_travel.plan.domain.api.reqeust.WayPointDetailUpdateRequest;
import click.porito.managed_travel.plan.domain.api.reqeust.pointer.DayPointable;
import click.porito.managed_travel.plan.domain.api.reqeust.pointer.WayPointPointable;
import click.porito.managed_travel.plan.domain.api.response.RouteResponse;
import click.porito.managed_travel.plan.Day;
import click.porito.managed_travel.plan.WayPoint;
import click.porito.managed_travel.plan.domain.exception.PlanNotFoundException;
import click.porito.managed_travel.plan.domain.exception.PointedComponentNotFoundException;
import click.porito.managed_travel.plan.domain.exception.InvalidRouteReorderRequestException;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;

public interface PlanRouteApi {

    /**
     * 여행의 하루 계획을 가져온다.
     * @param planId 여행 계획 ID
     * @param dayPointer day 포인터
     * @return 여행의 하루 계획
     * @throws PlanNotFoundException 여행 계획을 찾지 못한 경우
     * @throws PointedComponentNotFoundException 포인터가 가리키는 컴포넌트를 찾지 못한 경우
     */
    Day getDayPlan(String planId, @Valid DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException;

    /**
     * 새로운 Day를 추가한다
     * @param planId 여행 계획 ID
     * @param dayPointer 이 날짜의 뒤에 추가합니다. null 이면 맨 앞에 추가합니다.
     * @return 추가 후 경로
     * @throws PlanNotFoundException 여행 계획을 찾지 못한 경우
     * @throws PointedComponentNotFoundException 포인터가 가리키는 컴포넌트를 찾지 못한 경우
     */
    RouteResponse appendDayAfter(String planId, @Nullable @Valid DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException;

    /**
     * Day를 삭제한다. 비워지지 않을 경우 내부의 경유지를 순서를 유지한 채 여행에 속하게 한다.
     * @param planId 여행 계획 ID
     * @param dayPointer 삭제할 날짜
     * @return 삭제 후 경로
     */
    RouteResponse deleteDay(String planId, @Valid DayPointable dayPointer) throws PlanNotFoundException, PointedComponentNotFoundException;

    /**
     * 경유지를 가져온다.
     * @param planId 여행 계획 ID
     * @param wayPointPointer 경유지 포인터
     * @return 경유지
     * @throws PlanNotFoundException 여행 계획을 찾지 못한 경우
     * @throws PointedComponentNotFoundException 포인터가 가리키는 컴포넌트를 찾지 못한 경우
     */
    WayPoint getWayPoint(String planId, @Valid WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException;

    /**
     * 경유지를 업데이트한다.
     * @param planId 여행 계획 ID
     * @param wayPointPointer 경유지 포인터
     * @param request 업데이트 요청
     * @return 업데이트 후 경유지
     * @throws PlanNotFoundException 여행 계획을 찾지 못한 경우
     * @throws PointedComponentNotFoundException 포인터가 가리키는 컴포넌트를 찾지 못한 경우
     */
    WayPoint updateWayPoint(String planId,@Valid WayPointPointable wayPointPointer,@Valid WayPointDetailUpdateRequest request) throws PlanNotFoundException, PointedComponentNotFoundException;

    /**
     * 새로운 경유지를 삽입합니다.
     * <p>
     *     dayId == null && wayPointId == null 여행의 맨 앞에 삽입
     *     dayId != null && wayPointId == null 해당 day 의 맨 앞에 삽입
     *     dayId == null && wayPointId != null 여행에 속한(Day 에 속하지 않은) 경유지 뒤에 삽입
     *     dayId != null && wayPointId != null 해당 day 의 wayPointId 뒤에 삽입
     * </p>
     * @param planId 여행 계획 ID
     * @param request 경유지 삽입 정보
     * @return 삽입 후 경로
     * @throws PlanNotFoundException 여행 계획을 찾지 못한 경우
     * @throws PointedComponentNotFoundException 포인터가 가리키는 컴포넌트를 찾지 못한 경우
     * @throws InvalidRouteReorderRequestException 경로 재정렬 요청이 잘못된 경우
     */
    RouteResponse appendWayPointAfter(String planId, @Valid WayPointAppendAfterRequest request) throws PlanNotFoundException, PointedComponentNotFoundException, InvalidRouteReorderRequestException;


    RouteResponse deleteWayPoint(String planId, @Valid WayPointPointable wayPointPointer) throws PlanNotFoundException, PointedComponentNotFoundException;

    /**
     * 경유지를 이동한다.
     * @param planId 이동할 경유지가 속한 여행 계획 ID
     * @param request 덮어쓸 여행 순서, 모든 요소가 들어 있지 않으면 바뀌지 않는다.
     * @return 이동 후 경로
     */
    RouteResponse reorderRoute(String planId, @Valid ReorderRouteRequest request) throws PointedComponentNotFoundException, PlanNotFoundException, InvalidRouteReorderRequestException;

}
