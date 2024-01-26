package click.porito.account_common.api.response;

import lombok.Builder;

@Builder
public record AccountSummaryResponse(
        String userId,
        String name,
        String profileImgUri
) {
}
