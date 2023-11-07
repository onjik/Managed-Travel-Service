package click.porito.modular_travel.security.filter;

import click.porito.modular_travel.account.AccountDTO;
import click.porito.modular_travel.account.AccountInternalApi;
import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.security.component.OidcLoginFailureHandler;
import click.porito.modular_travel.security.exception.OidcInsufficientUserInfoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Validator;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class RegisterAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String processUrl = "/account/register";
    private final Validator validator;
    private final ObjectMapper objectMapper;
    private final AccountInternalApi accountApi;
    @Setter
    private OAuth2AuthorizedClientRepository authorizedClientRepository = new HttpSessionOAuth2AuthorizedClientRepository();
    public RegisterAuthenticationFilter(ObjectMapper objectMapper, Validator validator, AccountInternalApi accountInternalApi) {
        super(request -> request.getMethod().equals("POST") && processUrl.equals(request.getRequestURI()));
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.accountApi = accountInternalApi;

        super.setAuthenticationManager(authentication -> authentication); //no-op
        super.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"message\" : \"successfully registered and logged in\"}");
                writer.flush();
            }
        });
        super.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                if (exception instanceof UsernameNotFoundException) {
                    // 404 : 보안을 위해, 세션에 exception 이 저장되어 있지 않은 경우 404를 반환한다.
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                if (exception instanceof AuthenticationCredentialsNotFoundException || exception instanceof OidcInsufficientUserInfoException) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"message\" : \"invalid request\"}");
                    writer.flush();
                } else if (exception instanceof InternalAuthenticationServiceException) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"message\" : \"failed to register\"}");
                    writer.flush();
                }
            };
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        OidcInsufficientUserInfoException exception = parseException(request);
        final OidcUserRequest userRequest = exception.getUserRequest();
        AccountRegisterDTO registerForm = new AccountRegisterDTO(exception.getRegisterForm()); //copy - for prevent side effect

        registerForm.overrideInfo(parseBody(request));

        var violations = validator.validate(registerForm);
        if (!violations.isEmpty()){
            //bad request
            throw exception;
        }
        AccountDTO account = accountApi.registerAccount(registerForm);
        OidcUser oidcUser = account.toOidcUser(userRequest.getIdToken());

        try {
            OAuth2AuthenticationToken oauth2Authentication = new OAuth2AuthenticationToken(oidcUser, oidcUser.getAuthorities(), userRequest.getClientRegistration().getRegistrationId());
            saveAuthorizedClient(oauth2Authentication, userRequest, request, response);

            //remove exception from session
            request.getSession().removeAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY);

            return oauth2Authentication;
        } catch (ConstraintDeclarationException e){
            throw new InternalAuthenticationServiceException("failed to register", e);
        }
    }

    /**
     * @throws UsernameNotFoundException 만약 세션에 OidcInsufficientUserInfoException 이 없으면
     */
    protected OidcInsufficientUserInfoException parseException(HttpServletRequest request){
        Object object= request.getSession().getAttribute(OidcLoginFailureHandler.INSUFFICIENT_EXCEPTION_KEY);
        if (!(object instanceof OidcInsufficientUserInfoException)){
            throw new UsernameNotFoundException("Have No OidcInsufficientUserInfoException in session");
        }
        return (OidcInsufficientUserInfoException) object;
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

    protected void saveAuthorizedClient(OAuth2AuthenticationToken authentication, OidcUserRequest userRequest, HttpServletRequest request, HttpServletResponse response){
        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                userRequest.getClientRegistration(), authentication.getName(),
                userRequest.getAccessToken(), null);
        this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, authentication, request, response);
    }

}
