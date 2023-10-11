import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    private String REGEX = "(.*)\\.([^\\.]*)"; // Matches last occurrence of '/' and captures the filename and extension

    @Test
    void test() {
        Matcher matcher = Pattern.compile(REGEX).matcher("foo/bar/helloworld.png");
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals("foo/bar/helloworld", matcher.group(1));
        Assertions.assertEquals("png", matcher.group(2));
    }

    @Test
    void test2(){
        String srcKey = "foo/bar/helloworld.png";
        Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
        Assertions.assertTrue(matcher.matches());
        String extension = matcher.group(1);
        Assertions.assertEquals("png", extension);

    }
}
