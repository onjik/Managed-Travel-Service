package click.porito.account_common.event;

import click.porito.account_common.domain.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record AccountPutEvent (
        String userId,
        String name,
        String email,
        List<String> roles,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,
        Gender gender,
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate birthDate
){
}
