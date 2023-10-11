package click.porito.modular_travel.account.internal.util;

import org.springframework.core.io.ClassPathResource;
import click.porito.modular_travel.account.internal.exception.NameCreationIoException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 한글 단어 (형용사 + 명사) 를 조합하여, 의미 있는 랜덤한 이름을 생성해주는 유틸리티 클래스<br>
 * 단어 목록은 resources/dictionary 에서 참조한다.
 */
public class KoreanNameGenerator{

    public static String generate() {
        String adjective;
        String noun;
        try {
            adjective = getRandomAdjective();
            noun = getRandomNoun();
        } catch (IOException e){
            throw new NameCreationIoException();
        }
        return String.format("%s %s", adjective, noun);
    }

    private static String getRandomAdjective() throws IOException {

        ClassPathResource resource = new ClassPathResource("dictionary/korean_adjective.txt");
        String adjective = resource.getContentAsString(StandardCharsets.UTF_8);

        String[] split = adjective.split("\n");
        Random random = new Random();
        int i = random.nextInt(split.length - 1);
        return split[i];
    }

    private static String getRandomNoun() throws IOException {
        ClassPathResource resource = new ClassPathResource("dictionary/korean_animal_noun.txt");
        String noun = resource.getContentAsString(StandardCharsets.UTF_8);
        String[] split = noun.split("\n");
        Random random = new Random();
        int i = random.nextInt(split.length - 1);
        return split[i];
    }
}
