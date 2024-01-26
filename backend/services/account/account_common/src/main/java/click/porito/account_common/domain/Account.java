package click.porito.account_common.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Account {
    private String userId;
    private String name;
    private String email;
    private List<String> roles;
    private Instant createdAt;
    private Gender gender;
    private LocalDate birthDate;
    private String profileImgUri;
}