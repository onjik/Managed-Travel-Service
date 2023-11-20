package click.porito.modular_travel.place.exception;

import click.porito.modular_travel.place.exception.RestClientErrorException;
import click.porito.modular_travel.place.exception.RestServerErrorException;
import click.porito.modular_travel.place.exception.RestUnknownException;
import graphql.GraphQLError;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Order(0)
@ControllerAdvice
@RequiredArgsConstructor
public class PlaceExceptionHandler {

    @GraphQlExceptionHandler(ValidationException.class)
    public GraphQLError handle(ValidationException ex){
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage()).build();
    }


    @GraphQlExceptionHandler(RestClientErrorException.class)
    public GraphQLError handle(RestClientErrorException ex) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(RestServerErrorException.class)
    public GraphQLError handle(RestServerErrorException ex) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler(RestUnknownException.class)
    public GraphQLError handle(RestUnknownException ex) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message("Unknown Rest Error").build();
    }

}
