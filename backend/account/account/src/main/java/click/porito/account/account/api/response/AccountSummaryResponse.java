package click.porito.account.account.api.response;

import lombok.Builder;

@Builder
public record AccountSummaryResponse(
        String userId,
        String name,
        String profileImgUri
) {
}
