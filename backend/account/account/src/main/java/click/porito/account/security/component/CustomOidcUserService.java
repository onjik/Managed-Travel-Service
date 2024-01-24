package click.porito.account.security.component;

import click.porito.account.account.api.request.AccountRegisterRequest;
import click.porito.account.account.domain.Account;
import click.porito.account.account.domain.Gender;
import click.porito.account.account.operation.AccountOperation;
import click.porito.account.security.exception.InsufficientRegisterInfoException;
import click.porito.account.security.exception.OidcEmailNotVerifiedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;


/**
 * 스프링 시큐리티의 Oidc Login 을 처리하기 위한 핵심 컴포넌트인 {@link OAuth2UserService} 를 커스텀한 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final AccountOperation accountOperation;
    private final Validator validator;
    private OidcUserService oidcUserService = new OidcUserService();

    /**
     * 1. 이메일이 verified 되어 있는지 확인, 안되어 있으면,OidcEmailNotVerifiedException 발생
     * 2. 회원으로 등록되어 있는지 확인, 안되어 있으면 InsufficientRegisterInfoException 발생 -> 별도 핸들러에서 처리
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        boolean emailVerified = isEmailVerified(oidcUser);
        if (!emailVerified){
            throw new OidcEmailNotVerifiedException(oidcUser.getEmail(), userRequest.getClientRegistration().getClientName());
        }
        Account account = accountOperation.findByEmail(oidcUser.getEmail())
                .orElse(null);
        if (account == null){
            //check required info
            AccountRegisterRequest registerForm = createRegisterForm(oidcUser);
            List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            Set<ConstraintViolation<AccountRegisterRequest>> violations = validator.validate(registerForm);
            if (!violations.isEmpty()){
                //필수 정보가 부족한 경우
                throw new InsufficientRegisterInfoException(registerForm,violations);
            }
            //register
            account = accountOperation.create(roles, registerForm);
        }
        return toOidcUser(userRequest.getIdToken(), account);
    }

    protected OidcUser toOidcUser(final OidcIdToken idToken, Account account){
        Assert.notEmpty(idToken.getClaims(), "claims must not be empty");

        //idToken 의 sub(Subject) 을 account 의 userId 로 변경 하여 다시 생성
        HashMap<String, Object> map = new HashMap<>(idToken.getClaims());
        map.put(IdTokenClaimNames.SUB, account.getUserId());
        OidcIdToken token = new OidcIdToken(
                idToken.getTokenValue(),
                idToken.getIssuedAt(),
                idToken.getExpiresAt(),
                Collections.unmodifiableMap(map)
        );
        List<SimpleGrantedAuthority> authorities = account.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new DefaultOidcUser(authorities, token);
    }

    protected boolean isEmailVerified(OidcUser oidcUser){
        String email = oidcUser.getEmail();
        Boolean emailVerified = oidcUser.getEmailVerified();
        return email != null && emailVerified != null && emailVerified;
    }


    protected AccountRegisterRequest createRegisterForm(OidcUser oidcUser){
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

        return AccountRegisterRequest.builder()
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
