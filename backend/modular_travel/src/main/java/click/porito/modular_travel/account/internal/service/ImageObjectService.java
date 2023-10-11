package click.porito.modular_travel.account.internal.service;

import click.porito.modular_travel.account.internal.dto.AccountPrincipal;

import java.net.URL;

/**
 * 이미지 처리 서비스에 대한 추상화 인터페이스
 */
public interface ImageObjectService {
    URL getSignedProfilePutUrl(AccountPrincipal principal, String filename);

    void deleteProfileImage(AccountPrincipal principal);
}
