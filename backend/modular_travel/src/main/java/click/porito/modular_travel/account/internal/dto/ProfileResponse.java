package click.porito.modular_travel.account.internal.dto;

import click.porito.modular_travel.account.internal.entity.Account;

public record ProfileResponse(
        Long userId,
        String name,
        String profileImgUri
){
    public static ProfileResponse from(Account account) {
        return new ProfileResponse(
                account.getUserId(),
                account.getName(),
                account.getProfileImgUri()
        );
    }


}
