package click.porito.modular_travel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModuleStructureTest {

    @Test
    @DisplayName("Modular Monolith Structure Test")
    void test() {
        ApplicationModules.of(ModularTravelApplication.class).verify();
    }
}
