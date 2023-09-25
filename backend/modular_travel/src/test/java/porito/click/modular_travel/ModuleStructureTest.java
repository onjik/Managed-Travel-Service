package porito.click.modular_travel;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModuleStructureTest {

    @Test
    void test() {
        ApplicationModules.of(ModuleStructureTest.class).verify();
    }
}
