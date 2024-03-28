package click.porito.account_service.account.operation.adapter.mapper;

import click.porito.account_service.account.operation.adapter.persistence.entity.AccountEntity;
import click.porito.common.util.Mapper;
import click.porito.managed_travel.domain.domain.Account;
import click.porito.managed_travel.domain.domain.Gender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class AccountMapperConfig {

    @Bean
    public Mapper<AccountEntity, Account> toAccountMapper() {
        return new Mapper<AccountEntity, Account>() {
            @Override
            protected Account mapInternal(AccountEntity source) {
                String userId = source.getUserId() == null ? null : source.getUserId().toString();
                String name = source.getName();
                String email = source.getEmail();
                List<String> roles;
                if (source.getRoles() != null){
                    roles = source.getRoles();
                } else {
                    roles = new ArrayList<>();
                }
                Instant createdAt = source.getCreatedAt();
                Instant updatedAt = source.getUpdatedAt();
                Gender gender;
                if (source.getGender() != null){
                    gender = Gender.valueOf(source.getGender().name());
                } else {
                    gender = null;
                }
                LocalDate birthDate = source.getBirthDate();
                String profileImgUri = source.getProfileImgUri();

                return Account.builder()
                        .userId(userId)
                        .name(name)
                        .email(email)
                        .roles(roles)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .gender(gender)
                        .birthDate(birthDate)
                        .profileImgUri(profileImgUri)
                        .build();
            }
        };
    }

}
