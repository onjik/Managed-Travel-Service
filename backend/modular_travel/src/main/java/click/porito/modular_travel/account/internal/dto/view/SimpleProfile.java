package click.porito.modular_travel.account.internal.dto.view;

import click.porito.modular_travel.account.internal.entity.Account;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SimpleProfile {
    private Long userId;
    private String name;
    private String profileImgUri;

    public SimpleProfile(
            Long userId,
            String name,
            String profileImgUri
    ) {
        this.userId = userId;
        this.name = name;
        this.profileImgUri = profileImgUri;
    }

    public static SimpleProfile from(Account account) {
        return new SimpleProfile(
                account.getUserId(),
                account.getName(),
                account.getProfileImgUri()
        );
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SimpleProfile) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.profileImgUri, that.profileImgUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, profileImgUri);
    }

    @Override
    public String toString() {
        return "SimpleProfile[" +
                "userId=" + userId + ", " +
                "name=" + name + ", " +
                "profileImgUri=" + profileImgUri + ']';
    }


}
