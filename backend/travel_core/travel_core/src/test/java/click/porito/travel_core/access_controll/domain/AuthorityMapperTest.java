package click.porito.travel_core.access_controll.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityMapperTest {

    @Test
    @DisplayName("Regex test")
    void regexTest() {
        Pattern authorityPattern = AuthorityMapper.AUTHORITY_PATTERN;
        String authority = "place:read:all";
        var matcher = authorityPattern.matcher(authority);
        if (!matcher.find()) {
            throw new IllegalArgumentException("invalid authority: " + authority);
        }
        assertEquals("place", matcher.group("domain"));
        assertEquals("read", matcher.group("action"));
        assertEquals("all", matcher.group("target"));
    }

}