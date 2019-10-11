package tasks.transferservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.ZERO_AMOUNT;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.entity.Account.anAccount;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.AccountNotFoundException;
import tasks.transferservice.domain.exception.DuplicateAccountException;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultAccountService accountService;

    @Test
    public void createsAccountByRequest() throws DuplicateAccountException {
        CreateAccountRequest request = new CreateAccountRequest("request_id", "1111-2222", "EUR");

        when(accountRepository.save(any(Account.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        Account createdAccount = accountService.createAccount(request);

        assertThat(createdAccount)
            .extracting("accountNumber", "accountCurrency", "balance")
            .containsExactly(anAccountNumber("1111-2222"), EUR, ZERO_AMOUNT);
    }

    @Test
    public void depositsAccount() throws AccountNotFoundException {
        AccountNumber accountNumber = anAccountNumber("1111-2222");
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        DepositRequest depositRequest = new DepositRequest("request_id", depositAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(anAccount(accountNumber.getValue(), EUR));
        when(accountRepository.lockForUpdate(accountNumber)).thenReturn(anAccount(accountNumber.getValue(), EUR));

        accountService.deposit(accountNumber, depositRequest);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).update(accountCaptor.capture());

        Account updatedAccount = accountCaptor.getValue();

        assertThat(updatedAccount.getBalance()).isEqualTo(amountOf(depositAmount));
    }

    @Test
    public void failsToDepositNotExistingAccount() throws AccountNotFoundException {
        AccountNumber accountNumber = anAccountNumber("1111-2222");
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        DepositRequest depositRequest = new DepositRequest("request_id", depositAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        assertThatThrownBy(() -> accountService.deposit(accountNumber, depositRequest))
            .isInstanceOf(AccountNotFoundException.class);
        verify(accountRepository, never()).update(any());
    }
}