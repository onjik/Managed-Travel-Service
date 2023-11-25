package click.porito.modular_travel.security.component;

import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.security.constant.SecurityConstant;
import click.porito.modular_travel.security.exception.InsufficientRegisterInfoException;
import click.porito.modular_travel.security.exception.OidcEmailNotVerifiedException;
import click.porito.modular_travel.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import java.io.IOException;

import static org.mockito.Mockito.mock;

class DefaultLoginFailureHandlerTest {
    private JwtService jwtService;
    private DefaultLoginFailureHandler handler;

    @BeforeEach
    void setUp() {
        this.jwtService = mock(JwtService.class);
        this.handler = new DefaultLoginFailureHandler(new ObjectMapper(), jwtService);
    }

    @Test
    @DisplayName("InvalidCookieException 시 400")
    void invalidCookieException() throws ServletException, IOException {
        //given
        InvalidCookieException invalidCookie = new InvalidCookieException("invalid cookie");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        //when
        handler.onAuthenticationFailure(request, response, invalidCookie);

        //then
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("OidcEmailNotVerifiedException 시 401")
    void oidcEmailNotVerifiedException() throws ServletException, IOException {
        //given
        OidcEmailNotVerifiedException exception = new OidcEmailNotVerifiedException("email", "issuer");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        //when
        handler.onAuthenticationFailure(request, response, exception);

        //then
        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    @DisplayName("InsufficientRegisterInfoException 시 412 와 함께, 토큰화된 쿠키 발급")
    void insufficientRegisterInfoException() throws ServletException, IOException {
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        AccountRegisterDTO registerDTO = AccountRegisterDTO.builder()
                .name("name")
                .build();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        validatorFactory.close();

        var violations = validator.validate(registerDTO);
        InsufficientRegisterInfoException exception = new InsufficientRegisterInfoException(registerDTO, violations);
        //when
        handler.onAuthenticationFailure(request, response, exception);

        //then
        Assertions.assertEquals(412, response.getStatus());
        Assertions.assertNotNull(response.getCookie(SecurityConstant.INSUFFICIENT_FORM_TOKEN));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException 시 400")
    void authenticationCredentialsNotFoundException() throws ServletException, IOException {
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        //when
        handler.onAuthenticationFailure(request, response, new AuthenticationCredentialsNotFoundException("no cookie"));

        //then
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("InternalAuthenticationServiceException 시 500")
    void internalAuthenticationServiceException() throws ServletException, IOException {
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        //when
        handler.onAuthenticationFailure(request, response, new InternalAuthenticationServiceException("internal error"));

        //then
        Assertions.assertEquals(500, response.getStatus());
    }




}