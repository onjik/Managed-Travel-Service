package click.porito.modular_travel.account.internal.reposiotry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import click.porito.modular_travel.account.internal.entity.Account;
import click.porito.modular_travel.account.internal.util.KoreanNameGenerator;

import java.util.UUID;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void save() {
        //given
        Account account = Account.builder(UUID.randomUUID().toString() + "@test.com", Account.Role.ROLE_USER)
                .name(KoreanNameGenerator.generate())
                .build();
        //when
        Account save = accountRepository.save(account);
        //then
        Assertions.assertNotNull(save);

    }
}