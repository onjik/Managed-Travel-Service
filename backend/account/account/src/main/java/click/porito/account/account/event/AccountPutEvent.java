package click.porito.account.account.event;

import click.porito.account.account.Gender;
import click.porito.account.account.model.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.Instant;
import java.time.LocalDate;

public record AccountPutEvent (
        String userId,
        String name,
        String email,
        String[] roles,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,
        Gender gender,
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate birthDate
){
        public static AccountPutEvent from(Account account){
                return new AccountPutEvent(
                        account.getUserId().toString(),
                        account.getName(),
                        account.getEmail(),
                        account.getRoles().stream().map(Enum::name).toArray(String[]::new),
                        account.getCreatedAt(),
                        account.getGender(),
                        account.getBirthDate()
                );
        }
}
