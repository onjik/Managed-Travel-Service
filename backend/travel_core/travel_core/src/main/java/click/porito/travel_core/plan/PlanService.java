package click.porito.travel_core.plan;

import click.porito.travel_core.plan.dto.PlanCreateForm;
import click.porito.travel_core.plan.dto.PlanPutForm;
import click.porito.travel_core.plan.dto.PlanView;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface PlanService {
    /**
     * planId로 PlanView 를 조회한다.
     * @param planId 조회할 planId
     * @return 조회된 PlanView
     */
    Optional<PlanView> getPlan(String planId);


    /**
     * 주어진 여행계획을 포함한 여행 계획을 생성한다. 주어진 여행지 목록은 최적 위치에 자동 배치된다. 여행지가 주어지지 않았을 경우, 빈 여행 계획이 생성된다.
     * @param planCreateForm 생성할 여행 계획 정보
     * @throws IllegalArgumentException 인자의 값이 유효하지 않을 때(잘못된 placeId, 제약조건 위반 등)
     * @return 생성된 PlanView
     */
    PlanView createPlan(PlanCreateForm planCreateForm);

    /**
     * 특정 유저의 여행 계획 목록을 조회한다.
     * @param userId 조회할 유저의 id
     * @return 조회된 여행 계획 목록, 없으면 empty list
     */
    List<PlanView> getPlansOwnedBy(String userId);


    /**
     * 특정 유저의 여행 계획 목록을 조회한다.
     * @param userId 조회할 유저의 id
     * @param page 조회할 페이지 (min = 0)
     * @param size 한 페이지에 조회할 여행 계획 수 (min = 5, max =100)
     * @return 조회된 여행 계획 목록, 없으면 empty list
     */
    List<String> getPlanIdOwnedBy(String userId, @Nullable Integer page, @Nullable Integer size);

    /**
     * 유저의 여행 계획을 삭제한다.
     * @param planId 삭제할 여행 계획의 id
     * @return 삭제 성공 여부
     */
    boolean deletePlan(String planId);

    /**
     * 여행 계획의 정보를 수정한다.
     * @param planId 수정할 여행 계획의 id
     * @param planPutForm 수정할 여행 계획 정보
     * @return 수정된 PlanView
     */
    PlanView putPlanInfo(String planId, PlanPutForm planPutForm) throws InvalidUpdateInfoException, PlanOutOfDateException;


}
