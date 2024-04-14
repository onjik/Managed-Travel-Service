package click.porito.managed_travel.place.domain;

import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class ValidatorTest {


    @Test
    @DisplayName("만약 검증 어노테이션이 붙어 있지 않다면, 검증상 오류가 발생하지 않아야 한다.")
    void validateHasNoValidationAnnotation() {
        HasNoValidationAnnotation hasNoValidationAnnotation = new HasNoValidationAnnotation();
        hasNoValidationAnnotation.setName("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            var validator = validatorFactory.getValidator();
            var violations = validator.validate(hasNoValidationAnnotation);
            Assertions.assertTrue(violations.isEmpty());
        } catch (Exception e) {
            fail(e);
            throw new RuntimeException(e);
        }

    }

    @Test
    @DisplayName("List 안에 들어있는 객체는 검증이 되지 않는다.")
    void validatorDoesntValidateListElement() {
        HasList hasList = new HasList();
        HasList.Elem elem = new HasList.Elem();
        elem.name = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        hasList.elems = List.of(elem);

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            var validator = validatorFactory.getValidator();
            var violations = validator.validate(hasList);
            Assertions.assertTrue(violations.isEmpty());
        } catch (Exception e) {
            fail(e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("List 안에 들어있는 객체는 검증이 된다.")
    void validatorValidateListElement() {
        HasListWithValid hasList = new HasListWithValid();
        HasListWithValid.Elem elem = new HasListWithValid.Elem();
        elem.name = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        hasList.elems = List.of(elem);

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            var validator = validatorFactory.getValidator();
            var violations = validator.validate(hasList);
            Assertions.assertFalse(violations.isEmpty());
        } catch (Exception e) {
            fail(e);
            throw new RuntimeException(e);
        }
    }


    static class HasNoValidationAnnotation {
        private String name;
        public void createUserPlace() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class HasList {
        private List<Elem> elems;

        static class Elem {
            @Length(min = 1, max = 10)
            private String name;
        }
    }

    static class HasListWithValid {
        private List<@Valid Elem> elems;

        static class Elem {
            @Length(min = 1, max = 10)
            private String name;
        }
    }

}
