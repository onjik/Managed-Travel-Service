package click.porito.account.learning_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class InstanceOfTest {

    @Test
    void nullInstanceOfTest(){
        Object nullObject = null;
        Assertions.assertFalse(nullObject instanceof String);
    }
}
