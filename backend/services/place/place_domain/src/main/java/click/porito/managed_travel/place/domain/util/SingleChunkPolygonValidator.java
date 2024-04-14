package click.porito.managed_travel.place.domain.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.geojson.Polygon;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SingleChunkPolygonValidator implements ConstraintValidator<SingleChunkPolygonConstraint,Polygon> {
    @Override
    public void initialize(SingleChunkPolygonConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Polygon polygon, ConstraintValidatorContext constraintValidatorContext) {
        return polygon != null && polygon.getCoordinates().size() == 1;
    }
}
