package click.porito.travel_core.plan;

import click.porito.travel_core.plan.dto.PlanUpdateForm;
import click.porito.travel_core.plan.dto.PlanView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param planUpdateForm 생성할 여행 계획 정보
     * @throws IllegalArgumentException 인자의 값이 유효하지 않을 때(잘못된 placeId, 제약조건 위반 등)
     * @return 생성된 PlanView
     */
    PlanView createPlan(PlanUpdateForm planUpdateForm);

    /**
     * 특정 유저의 여행 계획 목록을 조회한다.
     * @param userId 조회할 유저의 id
     * @param pageable 페이지 정보
     * @return 조회된 여행 계획 목록, 없으면 empty list
     */
    Page<PlanView> getPlansOwnedBy(String userId, Pageable pageable);

    /**
     * 유저의 여행 계획을 삭제한다.
     * @param planId 삭제할 여행 계획의 id
     * @throws PlanDeleteProcessingException 여행 계획을 삭제 중 오류가 발생했을 때
     */
    void deletePlan(String planId);

    /**
     * 여행 계획의 정보를 수정한다.
     * @param planId 수정할 여행 계획의 id
     * @param planUpdateForm 수정할 여행 계획 정보
     * @return 수정된 PlanView
     */
    PlanView updatePlan(String planId, PlanUpdateForm planUpdateForm) throws InvalidUpdateInfoException, PlanVersionOutOfDateException;


}
