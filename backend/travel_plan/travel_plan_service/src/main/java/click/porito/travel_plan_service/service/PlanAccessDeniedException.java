package click.porito.travel_plan_service.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class PlanAccessDeniedException extends RuntimeException{
    private final PlanAccessManager.AccessType[] deniedOperations;

    public PlanAccessDeniedException(@NotBlank PlanAccessManager.AccessType[] deniedOperations) {
        Assert.notNull(deniedOperations, "deniedOperations must not be null");
        this.deniedOperations = deniedOperations;
    }

}
