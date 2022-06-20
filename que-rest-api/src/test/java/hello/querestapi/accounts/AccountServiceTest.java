package hello.querestapi.accounts;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;

import java.util.Set;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByUsername() {
        //given
        Account actual = Account.builder()
                .email("quedevel@naver.com")
                .password("quedevel")
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountRepository.save(actual);

        //when
        UserDetailsService userDetailsService = this.accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername("quedevel@naver.com");

        //then
        assertThat(userDetails.getPassword()).isEqualTo(actual.getPassword());
    }

    @Test
    void findByUsernameFail() {
        assertThrowsExactly(
                UsernameNotFoundException.class,
                () -> accountService.loadUserByUsername("random@email.com")
        );
    }
}