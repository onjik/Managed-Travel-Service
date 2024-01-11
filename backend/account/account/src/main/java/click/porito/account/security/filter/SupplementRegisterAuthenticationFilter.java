package click.porito.account.security.filter;

import click.porito.account.account.AccountDTO;
import click.porito.account.account.AccountRegisterDTO;
import click.porito.account.account.AccountService;
import click.porito.account.security.constant.SecurityConstant;
import click.porito.account.security.exception.InsufficientRegisterInfoException;
import click.porito.account.security.model.SimpleAuthentication;
import click.porito.account.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

public class SupplementRegisterAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final Validator validator;
    private final ObjectMapper objectMapper;
    private final AccountService accountApi;
    private final JwtService jwtService;

    public SupplementRegisterAuthenticationFilter(Validator validator, ObjectMapper objectMapper, AccountService accountApi, JwtService jwtService, AuthenticationFailureHandler failureHandler, AuthenticationSuccessHandler successHandler) {
        super(new AndRequestMatcher(
                request -> request.getMethod().equals(HttpMethod.POST.name()),
                request -> SecurityConstant.SUPPLEMENT_REGISTER_URI.equals(request.getRequestURI())
        ));
        this.validator = validator;
        this.objectMapper = objectMapper;
        this.accountApi = accountApi;
        this.jwtService = jwtService;
        super.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        super.setAuthenticationFailureHandler(failureHandler);
        super.setAuthenticationSuccessHandler(successHandler);
    }

    /**
     * @throws InsufficientRegisterInfoException : 필수 정보가 누락되었을 때
     * @throws InvalidCookieException : 쿠키가 유효하지 않을 때
     * @throws AuthenticationCredentialsNotFoundException 필요한 쿠기가 없을 때
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        AccountRegisterDTO supplement = parseBody(request);
        AccountRegisterDTO original = parseCookie(request);

        original.overrideInfo(supplement);

        Set<ConstraintViolation<AccountRegisterDTO>> violations = validator.validate(original);
        if (!violations.isEmpty()){
            throw new InsufficientRegisterInfoException(original, violations);
        }
        try {
            AccountDTO account = accountApi.registerAccount(original);
            return new SimpleAuthentication(account.getRoleNames(), account.getUserId().toString());
        } catch (DataIntegrityViolationException e) {
            //이미 등록한 유저를 JWT로 다시 등록하려고 할 때 도달 가능
            //JWT의 유효기간이 30분이므로, 30분 이내에 이러한 상황이 발생할 수 있음
            throw new InvalidCookieException("invalid request");
        } catch (Exception e){
            throw new InternalAuthenticationServiceException("Internal ServerError",e);
        } finally {
            //쿠키 삭제
            eraseJwtCookie(response);
        }
    }

    /**
     * @throws AuthenticationCredentialsNotFoundException 필요한 쿠기가 없을 때
     */
    protected AccountRegisterDTO parseCookie(HttpServletRequest request) throws AuthenticationCredentialsNotFoundException{
        //쿠키 값 찾아보기
        String key = SecurityConstant.INSUFFICIENT_FORM_TOKEN;
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            throw new AuthenticationCredentialsNotFoundException("no cookie");
        }
        Cookie cookie = Stream.of(cookies)
                .filter(c -> key.equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("no cookie"));

        String registerDtoToken = cookie.getValue();
        try {
            return jwtService.decodeRegisterDTO(registerDtoToken);
        } catch (Exception e){
            //쿠키가 유효하지 않을 때
            throw new BadCredentialsException("invalid request");
        }
    }

    protected AccountRegisterDTO parseBody(HttpServletRequest request) {
        //check if body exists
        if (request.getContentLength() == 0){
            throw new AuthenticationCredentialsNotFoundException("invalid request");
        }
        try {
            return objectMapper.readValue(request.getInputStream(), AccountRegisterDTO.class);
        } catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("invalid request");
        }
    }


    protected void eraseJwtCookie(HttpServletResponse response){
        Cookie cookie = new Cookie(SecurityConstant.INSUFFICIENT_FORM_TOKEN, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


}
