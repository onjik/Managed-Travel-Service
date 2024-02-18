package click.porito.managed_travel.domain.api.response;

import lombok.Builder;

@Builder
public record AccountSummaryResponse(
        String userId,
        String name,
        String profileImgUri
) {
}
