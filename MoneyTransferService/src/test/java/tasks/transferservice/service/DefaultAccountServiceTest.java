package tasks.transferservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.ZERO_AMOUNT;
import static tasks.transferservice.domain.common.Currency.EUR;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultAccountService accountService;

    @Test
    public void createsAccountByRequest() {
        CreateAccountRequest request = new CreateAccountRequest("request_id", "1111-2222", "EUR");

        when(accountRepository.save(any(Account.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        Account createdAccount = accountService.createAccount(request);

        assertThat(createdAccount)
            .extracting("accountNumber", "accountCurrency", "balance")
            .containsExactly(anAccountNumber("1111-2222"), EUR, ZERO_AMOUNT);
    }
}