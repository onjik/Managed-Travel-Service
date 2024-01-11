package click.porito.account.learning_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Disabled
public class JacksonTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void arrayParse(){
        String json = "[\"a\",\"b\",\"c\"]";
        String[] strings = null;
        try {
            strings = objectMapper.readValue(json, String[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
        System.out.println(strings.toString());
        assertEquals(3, strings.length);
        assertEquals("a", strings[0]);
        assertEquals("b", strings[1]);
        assertEquals("c", strings[2]);
    }
    @Test
    void test() throws JsonProcessingException {
        TestClass testClass = new TestClass("camelCase");
        String json = objectMapper.writeValueAsString(testClass);
        assertEquals("{\"camelCase\":\"camelCase\"}", json);
    }

    @Test
    @DisplayName("Locale 변환 테스트")
    void localeTest() throws JsonProcessingException {
        Locale korea = Locale.KOREA;
        String json = objectMapper.writeValueAsString(korea);
        assertEquals("\"ko_KR\"", json);
        Locale locale = objectMapper.readValue(json, Locale.class);
        assertEquals(korea, locale);
    }


    @Test
    void nullValueJson(){
        TestClass testClass = new TestClass(null);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(testClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
    }

    @Test
    @DisplayName("optional value json")
    void optional() throws JsonProcessingException {
        objectMapper.registerModule(new Jdk8Module());
        Optional<String> empty = Optional.empty();
        Optional<String> notEmpty = Optional.of("notEmpty");
        String emptyString = objectMapper.writeValueAsString(empty);
        String notEmptyString = objectMapper.writeValueAsString(notEmpty);
        Assertions.assertEquals("null", emptyString);
        Assertions.assertEquals("\"notEmpty\"", notEmptyString);

    }


    static class TestClass{
        private String camelCase;

        public TestClass(String camelCase) {
            this.camelCase = camelCase;
        }

        public TestClass() {
        }

        public String getCamelCase() {
            return camelCase;
        }
    }
}
