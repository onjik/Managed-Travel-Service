package click.porito.modular_travel.account.internal.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KoreanNameGeneratorTest {

    @Test
    @DisplayName("한국어 이름 생성 테스트")
    void generate() {
        String name = KoreanNameGenerator.generate();
        System.out.println(name);
        Assertions.assertNotNull(name);
    }

    @Test
    @Disabled
    @DisplayName("한국어 이름 생성 결과 예시 출력")
    void generateExample() {
        for (int i = 0; i < 20; i++) {
            String name = KoreanNameGenerator.generate();
            System.out.println(name);
        }
    }

}