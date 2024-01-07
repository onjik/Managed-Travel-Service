package click.porito.travel_plan_service.api.rest;

import click.porito.travel_plan_service.service.PlanAccessDeniedException;
import click.porito.travel_plan_service.service.PlanOutOfDateException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * PlanService 의 RestApi 에 대한 예외 처리 클래스
 * @see PlanRestApi
 */
@RestControllerAdvice(basePackageClasses = PlanRestApi.class)
public class PlanRestControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PlanNotFoundException.class)
    public ErrorAttributes handlePlanNotFoundException(PlanNotFoundException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ErrorAttributes.builder()
                .status(HttpStatus.NOT_FOUND)
                .exception(e)
                .path(path)
                .message("plan not found")
                .addDetail("planId", e.getPlanId())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    @ExceptionHandler(PlanNotModifiedException.class)
    public void handlePlanNotModifiedException() {
        // no payload
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PlanAccessDeniedException.class)
    public ErrorAttributes handlePlanAccessDeniedException(PlanAccessDeniedException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        var deniedOperations = e.getDeniedOperations();

        return ErrorAttributes.builder()
                .status(HttpStatus.FORBIDDEN)
                .exception(e)
                .path(path)
                .message("plan access denied")
                .addDetail("deniedOperations", deniedOperations)
                .build();
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(PlanOutOfDateException.class)
    public ErrorAttributes handlePlanOutOfDateException(PlanOutOfDateException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ErrorAttributes.builder()
                .status(HttpStatus.PRECONDITION_FAILED)
                .exception(e)
                .path(path)
                .message("plan version(Etag) is not matched")
                .build();
    }


}
