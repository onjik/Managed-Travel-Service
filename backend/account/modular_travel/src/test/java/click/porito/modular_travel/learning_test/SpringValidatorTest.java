package click.porito.modular_travel.learning_test;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
public class SpringValidatorTest {
    @Autowired
    private Validator validator;

    @Test
    void validatorInject(){
        assertNotNull(validator);
    }

    @Test
    void validateTest(){
        TestClass errorClass = new TestClass(null);
        Errors errors1 = new BeanPropertyBindingResult(errorClass, "errorClass");
        validator.validate(errorClass, errors1);
        assertTrue(errors1.hasErrors());

        TestClass validClass = new TestClass("name");
        Errors errors2 = new BeanPropertyBindingResult(validClass, "validClass");
        validator.validate(validClass, errors2);
        assertFalse(errors2.hasErrors());

    }

    @Test
    @DisplayName("만약 null이면 어떻게 검증할까?")
    void nullTest(){
        TestClass2 testClass = new TestClass2(null, null);
        Errors errors = new BeanPropertyBindingResult(testClass, "testClass2");
        validator.validate(testClass, errors);

        assertFalse(errors.hasErrors());




    }


    static class TestClass{
        @NotNull
        private String name;
        public TestClass(String name) {
            this.name = name;
        }
    }

    static class TestClass2{
        @Past Instant instant;
        @Length(min = 2, max = 20) String name;

        public TestClass2(Instant instant, String name) {
            this.instant = instant;
            this.name = name;
        }
    }
}
