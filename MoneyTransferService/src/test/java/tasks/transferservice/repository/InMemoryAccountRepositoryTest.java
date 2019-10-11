package tasks.transferservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.ZERO_AMOUNT;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.entity.Account.anAccount;

import org.junit.Test;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.common.Currency;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.AccountNotFoundException;
import tasks.transferservice.domain.exception.DuplicateAccountException;
import tasks.transferservice.repository.exception.EntityNotFoundException;

public class InMemoryAccountRepositoryTest {

    private static final AccountNumber ACCOUNT_NUMBER = anAccountNumber("1111");

    private InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();

    @Test
    public void savesAccount() throws DuplicateAccountException {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.save(account);

        Account savedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(savedAccount)
            .extracting("accountNumber", "accountCurrency", "balance")
            .containsExactly(anAccountNumber("1111"), Currency.EUR, ZERO_AMOUNT);
    }

    @Test
    public void preventsSavingAccountWithSameAccountNumber() throws DuplicateAccountException {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.save(account);

        Account otherAccount = anAccount("1111", Currency.USD);
        assertThatThrownBy(() -> accountRepository.save(otherAccount))
                .isInstanceOf(DuplicateAccountException.class)
                .hasMessage("Account 1111 already exists");
    }

    @Test
    public void findsAccountByAccountNumber() {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.restoreRepositoryStateFrom(account);

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(repoAccount.getAccountNumber()).isEqualTo(ACCOUNT_NUMBER);
    }

    @Test
    public void doesNotFindAccountByNonexistingAccountNumber() {
        Account account = anAccount("2222", Currency.EUR);

        accountRepository.restoreRepositoryStateFrom(account);

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(repoAccount).isNull();
    }

    @Test
    public void ensuresSafePublicationOnSaveOfResult() throws DuplicateAccountException {
        Account account = anAccount("1111", Currency.EUR);
        Account savedAccount = accountRepository.save(account);
        savedAccount.credit(amountOf("100"));

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(repoAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
        assertThat(savedAccount.getBalance()).isEqualTo(amountOf("100"));
    }

    @Test
    public void ensuresSafePublicationOnSaveOfParameter() throws DuplicateAccountException {
        Account account = anAccount("1111", Currency.EUR);

        accountRepository.save(account);
        account.credit(amountOf("100"));

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(repoAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
        assertThat(account.getBalance()).isEqualTo(amountOf("100"));
    }

    @Test
    public void ensuresSafePublicationOnFindByAccountNumber() throws DuplicateAccountException {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.save(account);


        Account savedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        savedAccount.credit(amountOf("100"));

        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(retrievedAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
        assertThat(savedAccount.getBalance()).isEqualTo(amountOf("100"));
    }

    @Test
    public void ensuresSafePublicationOnRestoringState() {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.restoreRepositoryStateFrom(account);

        account.credit(Amount.amountOf("10"));

        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(retrievedAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
    }

    @Test
    public void updatesAccount() throws AccountNotFoundException {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.restoreRepositoryStateFrom(account);

        account.credit(Amount.amountOf("10"));

        Account updatedAccount = accountRepository.update(account);
        assertThat(updatedAccount.getBalance()).isEqualTo(amountOf("10"));

        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(retrievedAccount.getBalance()).isEqualTo(amountOf("10"));
    }

    @Test
    public void failsToUpdateNotExistingAccount() {
        Account account = anAccount("1111", Currency.EUR);
        account.credit(Amount.amountOf("10"));

        assertThatThrownBy(() -> accountRepository.update(account))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Entity of tasks.transferservice.domain.entity.Account with id 1111 can't be found");

        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(retrievedAccount).isNull();
    }

    @Test
    public void ensuresSafePublicationOnUpdateOfParameter() throws AccountNotFoundException {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.restoreRepositoryStateFrom(account);

        account.credit(Amount.amountOf("10"));

        accountRepository.update(account);
        account.credit(Amount.amountOf("30"));

        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(retrievedAccount.getBalance()).isEqualTo(amountOf("10"));
        assertThat(account.getBalance()).isEqualTo(amountOf("40"));
    }

    @Test
    public void ensuresSafePublicationOnUpdateOfResult() throws AccountNotFoundException {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.restoreRepositoryStateFrom(account);

        account.credit(amountOf("10"));
        Account updatedAccount = accountRepository.update(account);

        updatedAccount.credit(Amount.amountOf("30"));
        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(retrievedAccount.getBalance()).isEqualTo(amountOf("10"));
        assertThat(updatedAccount.getBalance()).isEqualTo(amountOf("40"));
    }

}