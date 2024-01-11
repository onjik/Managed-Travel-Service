package click.porito.account.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonDeserialize(as = AccountSummaryDTO.AccountSummaryDTOImpl.class)
public interface AccountSummaryDTO {

    Long getUserId();
    String getName();
    String getProfileImgUri();

    @Getter
    @Builder
    @RequiredArgsConstructor
    static class AccountSummaryDTOImpl implements AccountSummaryDTO {
        private final Long userId;
        private final String name;
        private final String profileImgUri;

    }

}
