package click.porito.travel_plan_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 여행 계획에 접근 권한을 체크 해주는 클래스
 */
public interface PlanAccessManager {
    /**
     * 주어진 사용자가 주어진 여행 계획에 접근할 수 있는지 확인한다.
     * @param requester 접근하려는 사용자
     * @param planId 여행 계획 id
     * @param operations 접근하려는 작업 목록
     * @throws PlanAccessDeniedException 접근 권한이 없는 경우
     */
    void checkPlaceAccessible(Requester requester, String planId, AccessType[] operations) throws PlanAccessDeniedException;

    /**
     * 주어진 사용자가 소유한 여행 계획에 접근할 수 있는지 확인한다.
     * @param ownerId 여행 계획 소유자 id
     * @param requester 접근하려는 사용자
     * @param operations 접근하려는 작업 목록
     * @throws PlanAccessDeniedException 접근 권한이 없는 경우
     */
    void checkPlaceOwnedByAccessible(String ownerId, Requester requester, AccessType[] operations) throws PlanAccessDeniedException;

    /**
     * 주어진 사용자가 여행 계획을 생성할 수 있는지 확인한다.
     * @param requester 접근하려는 사용자
     * @throws PlanAccessDeniedException 접근 권한이 없는 경우
     */
    void checkCreatable(Requester requester) throws PlanAccessDeniedException;


    default CheckMethodLinker can(String userId, String[] roles){
        return can(Requester.of(userId, roles));
    }

    default CheckMethodLinker can(Requester requester){
        return new CheckMethodLinker(this, requester);
    }

    class CheckMethodLinker {
        private final PlanAccessManager planAccessManager;
        private final Requester requester;

        private CheckMethodLinker(PlanAccessManager planAccessManager, Requester requester) {
            this.planAccessManager = planAccessManager;
            this.requester = requester;
        }
        public OperationBuilder accessToPlan(final String planId) throws PlanAccessDeniedException {
            Consumer<AccessType[]> checkFunction = operations -> planAccessManager.checkPlaceAccessible(requester, planId, operations);
            return new PlanAccessManager.OperationBuilder(checkFunction);
        }

        public OperationBuilder accessToPlacesOwnedBy(final String ownerId) throws PlanAccessDeniedException {
            Consumer<AccessType[]> checkFunction = operations -> planAccessManager.checkPlaceOwnedByAccessible(ownerId, requester, operations);
            return new PlanAccessManager.OperationBuilder(checkFunction);
        }

        public void createPlan() throws PlanAccessDeniedException {
            planAccessManager.checkCreatable(requester);
        }
    }


    enum AccessType {
        READ, UPDATE, DELETE
    }

    class OperationBuilder {
        private final Consumer<AccessType[]> checkFunction;
        private final List<AccessType> operations = new ArrayList<>(4);

        private OperationBuilder(Consumer<AccessType[]> checkFunction) {
            this.checkFunction = checkFunction;
        }

        public OperationBuilder read() {
            operations.add(AccessType.READ);
            return this;
        }

        public OperationBuilder update() {
            operations.add(AccessType.UPDATE);
            return this;
        }

        public OperationBuilder delete() {
            operations.add(AccessType.DELETE);
            return this;
        }

        public void check() throws PlanAccessDeniedException{
            checkFunction.accept(operations.toArray(AccessType[]::new));
        }

    }
}
