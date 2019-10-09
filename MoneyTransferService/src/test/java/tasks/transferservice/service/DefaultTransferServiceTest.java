package tasks.transferservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.common.Currency.USD;
import static tasks.transferservice.domain.entity.Account.anAccount;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.InvalidTransferException;
import tasks.transferservice.domain.rest.ExecuteTransferRequest;
import tasks.transferservice.repository.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTransferServiceTest {

    private static final AccountNumber CREDIT_ACCOUNT_NUMBER = anAccountNumber("credit_account_number");
    private static final AccountNumber DEBIT_ACCOUNT_NUMBER = anAccountNumber("debit_account_number");

    private ExecuteTransferRequest transferRequest;
    private Account creditAccount;
    private Account debitAccount;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultTransferService transferService;

    @Before
    public void setUp() {
        transferRequest = new ExecuteTransferRequest();
        transferRequest.setCreditAccount(CREDIT_ACCOUNT_NUMBER.getValue());
        transferRequest.setDebitAccount(DEBIT_ACCOUNT_NUMBER.getValue());
        transferRequest.setTransferAmount(BigDecimal.TEN);

        creditAccount = anAccount(CREDIT_ACCOUNT_NUMBER.getValue(), EUR);
        debitAccount = anAccount(DEBIT_ACCOUNT_NUMBER.getValue(), EUR);

        when(accountRepository.findByAccountNumber(CREDIT_ACCOUNT_NUMBER)).thenReturn(creditAccount);
        when(accountRepository.findByAccountNumber(DEBIT_ACCOUNT_NUMBER)).thenReturn(debitAccount);
        when(accountRepository.lockForUpdate(CREDIT_ACCOUNT_NUMBER)).thenReturn(creditAccount);
        when(accountRepository.lockForUpdate(DEBIT_ACCOUNT_NUMBER)).thenReturn(debitAccount);
    }

    @Test
    public void raisesExceptionIfDebitAccountDoesNotExist() {
        when(accountRepository.lockForUpdate(DEBIT_ACCOUNT_NUMBER)).thenReturn(null);
        when(accountRepository.findByAccountNumber(DEBIT_ACCOUNT_NUMBER)).thenReturn(null);

        assertThatThrownBy(() -> transferService.transfer(transferRequest))
            .isInstanceOf(InvalidTransferException.class)
            .hasMessage("Account 'debit_account_number' does not exist");

        verify(accountRepository, never()).update(any());
    }

    @Test
    public void raisesExceptionIfCreditAccountDoesNotExist() {
        when(accountRepository.lockForUpdate(CREDIT_ACCOUNT_NUMBER)).thenReturn(null);
        when(accountRepository.findByAccountNumber(CREDIT_ACCOUNT_NUMBER)).thenReturn(null);

        assertThatThrownBy(() -> transferService.transfer(transferRequest))
            .isInstanceOf(InvalidTransferException.class)
            .hasMessage("Account 'credit_account_number' does not exist");

        verify(accountRepository, never()).update(any());
    }

    @Test
    public void raisesExceptionIfAccountsHaveDifferentCurrencies() {
        Account debitAccount = anAccount(DEBIT_ACCOUNT_NUMBER.getValue(), USD);
        when(accountRepository.lockForUpdate(DEBIT_ACCOUNT_NUMBER)).thenReturn(debitAccount);
        when(accountRepository.findByAccountNumber(DEBIT_ACCOUNT_NUMBER)).thenReturn(debitAccount);

        assertThatThrownBy(() -> transferService.transfer(transferRequest))
            .isInstanceOf(InvalidTransferException.class)
            .hasMessage("Counterparty accounts have different currencies");

        verify(accountRepository, never()).update(any());
    }

    @Test
    public void raisesExceptionIfDebitAccountsHaveInsufficientFunds() {

        assertThatThrownBy(() -> transferService.transfer(transferRequest))
            .isInstanceOf(InvalidTransferException.class)
            .hasMessage("Debit account does not have enough funds to complete transfer");

        verify(accountRepository, never()).update(any());
    }

    @Test
    public void completesValidTransfer() {
        debitAccount.credit(amountOf("30"));

        transferService.transfer(transferRequest);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(2)).update(accountCaptor.capture());

        assertThat(accountCaptor.getAllValues())
            .extracting("accountNumber", "balance")
            .containsExactlyInAnyOrder(
                tuple(CREDIT_ACCOUNT_NUMBER, amountOf(BigDecimal.TEN)),
                tuple(DEBIT_ACCOUNT_NUMBER, amountOf("20")));
    }

}