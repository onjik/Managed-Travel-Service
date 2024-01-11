package click.porito.account.security.filter;

import click.porito.account.security.model.SimpleAuthentication;
import click.porito.account.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class JwtPublishFilterTest {

    private JwtService service;
    private JwtPublishFilter jwtPublishFilter;

    @BeforeEach
    void setUp() {
        this.service= mock(JwtService.class);
        this.jwtPublishFilter = new JwtPublishFilter(service);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("이번 요청 중에 인증이 되었다면, 응답에 JWT를 실어서 보내준다.")
    void authenticatedOnThisRequest() throws ServletException, IOException {
        //given
        SecurityContextHolder.clearContext();//명시적으로 인증을 지워준다.
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        doAnswer(invocation -> {
            SimpleAuthentication authentication = new SimpleAuthentication(List.of("USER"), "1");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return null;
        }).when(chain).doFilter(request, response);

        //when
        jwtPublishFilter.doFilter(request, response, chain);

        //then
        String value = response.getHeader(HttpHeaders.AUTHORIZATION);
        Assertions.assertNotNull(value);
    }

    @Test
    @DisplayName("원래 인증이 되어있었다면, 응답에 JWT를 실어서 보내지 않는다.")
    void authenticatedOnBeforeRequest() throws ServletException, IOException {
        //given
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        //이미 인증이 되어있다고 가정한다.
        Authentication authentication = new SimpleAuthentication(List.of("USER"), "1");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        doAnswer(invocation -> {
            return null;
        }).when(chain).doFilter(request, response);

        //when
        jwtPublishFilter.doFilter(request, response, chain);

        //then
        verify(service, never()).encode(any());
        String value = response.getHeader(HttpHeaders.AUTHORIZATION);
        Assertions.assertNull(value);
    }

}