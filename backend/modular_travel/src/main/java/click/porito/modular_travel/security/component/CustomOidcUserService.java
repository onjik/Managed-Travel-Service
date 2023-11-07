package click.porito.modular_travel.security.component;

import click.porito.modular_travel.account.AccountDTO;
import click.porito.modular_travel.account.AccountInternalApi;
import click.porito.modular_travel.account.AccountRegisterDTO;
import click.porito.modular_travel.account.Gender;
import click.porito.modular_travel.security.exception.OidcEmailNotVerifiedException;
import click.porito.modular_travel.security.exception.OidcInsufficientUserInfoException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;


/**
 * 스프링 시큐리티의 Oidc Login 을 처리하기 위한 핵심 컴포넌트인 {@link OAuth2UserService} 를 커스텀한 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final AccountInternalApi accountApi;
    private final Validator validator;
    private OidcUserService oidcUserService = new OidcUserService();

    /**
     * 1. 이메일이 verified 되어 있는지 확인, 안되어 있으면,OidcEmailNotVerifiedException 발생
     * 2. 회원으로 등록되어 있는지 확인, 안되어 있으면 OidcInsufficientUserInfoException 발생 -> 별도 핸들러에서 처리
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        boolean emailVerified = isEmailVerified(oidcUser);
        if (!emailVerified){
            throw new OidcEmailNotVerifiedException(oidcUser.getEmail(), userRequest.getClientRegistration().getClientName());
        }
        AccountDTO account = accountApi.getAccountByEmail(oidcUser.getEmail())
                .orElse(null);
        if (account == null){
            //check required info
            AccountRegisterDTO registerForm = createRegisterForm(oidcUser);
            Set<ConstraintViolation<AccountRegisterDTO>> violations = validator.validate(registerForm);
            if (!violations.isEmpty()){
                //필수 정보가 부족한 경우
                throw new OidcInsufficientUserInfoException(userRequest, registerForm,violations);
            }
            //register
            account = accountApi.registerAccount(registerForm);
        }
        return account.toOidcUser(userRequest.getIdToken());
    }

    protected boolean isEmailVerified(OidcUser oidcUser){
        String email = oidcUser.getEmail();
        Boolean emailVerified = oidcUser.getEmailVerified();
        return email != null && emailVerified != null && emailVerified;
    }


    protected AccountRegisterDTO createRegisterForm(OidcUser oidcUser){
        Gender gender = Stream.of(oidcUser.getGender())
                .filter(Objects::nonNull)
                .map(String::toUpperCase)
                .map(Gender::valueOf)
                .findFirst()
                .orElse(null);
        //YYYY-MM-DD format
        LocalDate birthDate = Stream.of(oidcUser.getBirthdate())
                .filter(Objects::nonNull)
                .map(LocalDate::parse)
                .findFirst()
                .orElse(null);

        return AccountRegisterDTO.builder()
                .email(oidcUser.getEmail())
                .name(oidcUser.getFullName())
                .gender(gender)
                .birthDate(birthDate)
                .build();
    }


    public void setOidcUserService(OidcUserService oidcUserService) {
        this.oidcUserService = oidcUserService;
    }






}
