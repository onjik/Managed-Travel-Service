package click.porito.modular_travel.learning_test;

import click.porito.modular_travel.account.AccountRegisterDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

@Disabled
@ActiveProfiles("test")
@SpringBootTest
public class ValidateTest {
    @Autowired
    private Validator validator;
    @Test
    void test() {
        AccountRegisterDTO form = AccountRegisterDTO.builder()
                .build();
        Set<ConstraintViolation<AccountRegisterDTO>> validate = validator.validate(form);

        Assertions.assertEquals(3, validate.size());
    }
}
