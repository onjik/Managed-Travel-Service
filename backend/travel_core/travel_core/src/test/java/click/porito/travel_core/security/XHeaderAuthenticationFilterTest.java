package click.porito.travel_core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//unit test
class XHeaderAuthenticationFilterTest {

    private XHeaderAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = spy(new XHeaderAuthenticationFilter(new ObjectMapper()));
        SecurityContextHolder.clearContext();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("Header 검증")
    class requiresAuthentication{

        @Test
        @DisplayName("만약 X-Authorization-Id 헤더가 없다면 인증을 시도하지 않는다.")
        void noHeader() throws IOException, ServletException {
            //given

            //when
            filter.doFilter(request, response,filterChain);

            //then - attemptAuthentication 이  호출이 되지 않음
            verify(filter, never()).attemptAuthentication(request, response);
            verify(filter, times(1)).doFilter(request, response, filterChain);

        }

        @Test
        @DisplayName("만약 X-Authorization-Id 헤더가 있다면 인증을 시도한다.")
        void hasHeader() throws IOException, ServletException {
            //given
            request.addHeader("X-Authorization-Id", "test");
            request.addHeader("X-Authorization-Roles", "[\"USER\", \"ADMIN\"]");

            //when
            filter.doFilter(request, response,filterChain);

            //then - attemptAuthentication 이  호출이 되지 않음
            verify(filter, times(1)).attemptAuthentication(request, response);
            verify(filter, times(1)).doFilter(request, response, filterChain);
        }
        @Test
        @DisplayName("ROLE의 형식이 잘못 되었을 경우 401 응답을 보낸다.")
        void attemptAuthenticationFailResponse2() throws IOException, ServletException {
            //given
            request.addHeader("X-Authorization-Id", "test");
            request.addHeader("X-Authorization-Roles", "[\"USER\", \"ADMIN");

            //when
            filter.doFilter(request, response,filterChain);

            //then
            assertEquals(401, response.getStatus());
        }
    }

    @Test
    @DisplayName("인증 성공시, SecurityContextHolder 에 인증 정보를 저장한다.")
    void attemptAuthentication() throws IOException, ServletException {
        //given
        request.addHeader("X-Authorization-Id", "test");
        request.addHeader("X-Authorization-Roles", "[\"USER\", \"ADMIN\"]");

        //when
        filter.doFilter(request, response,filterChain);

        //then
        assertEquals("test", SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        List<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> a.getAuthority())
                .toList();
        assertTrue(authorities.containsAll(List.of("USER", "ADMIN")));
    }

    @Test
    @DisplayName("인증 실패시, SecurityContextHolder 에 인증 정보를 저장하지 않는다.")
    void attemptAuthenticationFail() throws IOException, ServletException {
        //given

        //when
        filter.doFilter(request, response,filterChain);

        //then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }





}