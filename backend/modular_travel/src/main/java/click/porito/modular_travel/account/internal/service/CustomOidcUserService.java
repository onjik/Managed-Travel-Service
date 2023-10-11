package click.porito.modular_travel.account.internal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import click.porito.modular_travel.account.internal.config.AccountSecurityConfig;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.reposiotry.AccountRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.IS_NEW_ACCOUNT;
import static click.porito.modular_travel.account.internal.dto.AccountPrincipal.AttributeName.USER_ID;


/**
 * 스프링 시큐리티의 Oidc Login 을 처리하기 위한 핵심 컴포넌트인 {@link OAuth2UserService} 를 커스텀한 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final AccountRepository accountRepository;
    private OidcUserService oidcUserService = new OidcUserService();

    /**
     * 기본 OidcUserService를 사용하여 OidcUser를 로드하고 추가 작업을 진행한다. <br>
     * 1. Account 가 존재하지 않으면 생성한다. <br>
     * 2. 생성 여부에 따라 isNewAccount 를 설정한다 <br>
     * 3. userId 와 isNewAccount 를 OidcUser 에 추가한다. 이는 추후 유저 인식과 {@link AccountSecurityConfig.OidcLoginSuccessHandler} 에서 사용된다.<br>
     * @param userRequest the user request
     * @return 추가 클레임이 추가된 OidcUser
     * @throws OAuth2AuthenticationException 처리 중 예외시,
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        try {
            log.info("Try to load oidcUser(email: {})", oidcUser.getEmail());
            return doAdditionalProcess(oidcUser);
        } catch (Throwable e){
            log.error("OidcUser Additional Process Error", e);
            OAuth2Error error = new OAuth2Error("OidcUser Additional Process Error");
            throw new OAuth2AuthenticationException(error, e);
        }
    }

    @Transactional
    public OidcUser doAdditionalProcess(OidcUser oidcUser) {
        boolean isNewAccount;
        Optional<Account> optional = accountRepository.findByEmail(oidcUser.getEmail());
        Account account;
        if (optional.isPresent()) {
            isNewAccount = false;
            account = optional.get();
            log.debug("Account(userId: {}, email: {}) is already exist", account.getUserId(), oidcUser.getEmail());
        } else {
            isNewAccount = true;
            account = accountRepository.save(Account.from(oidcUser, Account.Role.ROLE_USER));
            log.debug("Account(userId: {}, email: {}) is created", account.getUserId(), oidcUser.getEmail());
        }
        //store custom attributes
        Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
        attributes.put(IS_NEW_ACCOUNT, isNewAccount);
        attributes.put(USER_ID, account.getUserId());
        Map<String, Object> safeMap = Collections.unmodifiableMap(attributes); //보안을 위해 unmodifiableMap 으로 변경
        OidcUserInfo oidcUserInfo = new OidcUserInfo(safeMap);
        return new DefaultOidcUser(account.getRoles(), oidcUser.getIdToken(), oidcUserInfo);
    }

    public void setOidcUserService(OidcUserService oidcUserService) {
        this.oidcUserService = oidcUserService;
    }






}
