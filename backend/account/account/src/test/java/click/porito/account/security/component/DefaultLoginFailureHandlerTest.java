package click.porito.account.security.component;

import click.porito.account.account.AccountRegisterDTO;
import click.porito.account.security.event.AuthenticationFailEvent;
import click.porito.account.security.event.SecurityTopics;
import click.porito.account.security.exception.InsufficientRegisterInfoException;
import click.porito.account.security.exception.OidcEmailNotVerifiedException;
import click.porito.account.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class DefaultLoginFailureHandlerTest {
    private JwtService jwtService;
    private DefaultLoginFailureHandler handler;
    private KafkaTemplate<String,Object> kafkaTemplate;

    record ExceptionAndStatus(
            AuthenticationException exception,
            int status
    ) {
    }

    private static Stream<ExceptionAndStatus> exceptionAndStatuses() {
        Stream<Supplier<ExceptionAndStatus>> stream = Stream.of(
                () -> new ExceptionAndStatus(new InvalidCookieException("invalid cookie"), 400),
                () -> new ExceptionAndStatus(new OidcEmailNotVerifiedException("email", "issuer"), 401),
                () -> {
                    AccountRegisterDTO registerDTO = AccountRegisterDTO.builder()
                            .name("name")
                            .build();
                    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    var validator = validatorFactory.getValidator();
                    validatorFactory.close();

                    var violations = validator.validate(registerDTO);
                    InsufficientRegisterInfoException exception = new InsufficientRegisterInfoException(registerDTO, violations);
                    return new ExceptionAndStatus(exception, 412);
                },
                () -> new ExceptionAndStatus(new AuthenticationCredentialsNotFoundException("no cookie"), 400),
                () -> new ExceptionAndStatus(new InternalAuthenticationServiceException("internal error"), 500)
        );
        return stream.map(Supplier::get);
    }

    @BeforeEach
    void setUp() {
        this.jwtService = mock(JwtService.class);
        this.kafkaTemplate = mock(KafkaTemplate.class);
        this.handler = new DefaultLoginFailureHandler(new ObjectMapper(), jwtService, kafkaTemplate);
    }

    @ParameterizedTest
    @MethodSource("exceptionAndStatuses")
    @DisplayName("예외에 맞는 에러코드를 보내고, 실패 이벤트를 발송한다.")
    void translationException(ExceptionAndStatus exceptionAndStatus) throws ServletException, IOException {
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        //when
        handler.onAuthenticationFailure(request, response, exceptionAndStatus.exception);

        //then
        Assertions.assertEquals(exceptionAndStatus.status, response.getStatus());
        verify(kafkaTemplate,times(1)).send(eq(SecurityTopics.AUTHENTICATION_FAILURE_0), any(AuthenticationFailEvent.class));
    }

//    @Test
//    @DisplayName("InvalidCookieException 시 400")
//    void invalidCookieException() throws ServletException, IOException {
//        //given
//        InvalidCookieException invalidCookie = new InvalidCookieException("invalid cookie");
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        //when
//        handler.onAuthenticationFailure(request, response, invalidCookie);
//
//        //then
//        Assertions.assertEquals(400, response.getStatus());
//    }
//
//    @Test
//    @DisplayName("OidcEmailNotVerifiedException 시 401")
//    void oidcEmailNotVerifiedException() throws ServletException, IOException {
//        //given
//        OidcEmailNotVerifiedException exception = new OidcEmailNotVerifiedException("email", "issuer");
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        //when
//        handler.onAuthenticationFailure(request, response, exception);
//
//        //then
//        Assertions.assertEquals(401, response.getStatus());
//    }
//
//    @Test
//    @DisplayName("InsufficientRegisterInfoException 시 412 와 함께, 토큰화된 쿠키 발급")
//    void insufficientRegisterInfoException() throws ServletException, IOException {
//        //given
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        AccountRegisterDTO registerDTO = AccountRegisterDTO.builder()
//                .name("name")
//                .build();
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        var validator = validatorFactory.getValidator();
//        validatorFactory.close();
//
//        var violations = validator.validate(registerDTO);
//        InsufficientRegisterInfoException exception = new InsufficientRegisterInfoException(registerDTO, violations);
//        //when
//        handler.onAuthenticationFailure(request, response, exception);
//
//        //then
//        Assertions.assertEquals(412, response.getStatus());
//        Assertions.assertNotNull(response.getCookie(SecurityConstant.INSUFFICIENT_FORM_TOKEN));
//    }
//
//    @Test
//    @DisplayName("AuthenticationCredentialsNotFoundException 시 400")
//    void authenticationCredentialsNotFoundException() throws ServletException, IOException {
//        //given
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        //when
//        handler.onAuthenticationFailure(request, response, new AuthenticationCredentialsNotFoundException("no cookie"));
//
//        //then
//        Assertions.assertEquals(400, response.getStatus());
//    }
//
//    @Test
//    @DisplayName("InternalAuthenticationServiceException 시 500")
//    void internalAuthenticationServiceException() throws ServletException, IOException {
//        //given
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        //when
//        handler.onAuthenticationFailure(request, response, new InternalAuthenticationServiceException("internal error"));
//
//        //then
//        Assertions.assertEquals(500, response.getStatus());
//    }




}