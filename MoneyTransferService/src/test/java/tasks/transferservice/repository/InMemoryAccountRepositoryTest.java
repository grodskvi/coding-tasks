package tasks.transferservice.repository;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static tasks.transferservice.domain.common.AccountDomainKey.anAccountDomainKey;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.ZERO_AMOUNT;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.entity.Account.anAccount;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.common.Currency;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.repository.exception.AlreadyPersistedEntityException;
import tasks.transferservice.repository.exception.DuplicateEntityException;
import tasks.transferservice.service.DomainKeyProvider;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryAccountRepositoryTest {

    private static final AccountNumber ACCOUNT_NUMBER = anAccountNumber("1111");

    @Mock
    private DomainKeyProvider domainKeyProvider;

    @InjectMocks
    private InMemoryAccountRepository accountRepository;

    @Before
    public void setUp() {
        when(domainKeyProvider.nextDomainKey()).thenReturn("account_domain_key");
    }

    @Test
    public void savesAccount() {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.save(account);

        Account savedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(savedAccount)
            .extracting("domainKey", "accountNumber", "accountCurrency", "balance")
            .containsExactly(anAccountDomainKey("account_domain_key"), anAccountNumber("1111"), Currency.EUR, ZERO_AMOUNT);
    }

    @Test
    public void generatesAccountKeyOnSave() {
        Account account = anAccount("1111", Currency.EUR);
        Account savedAccount = accountRepository.save(account);

        assertThat(savedAccount.getDomainKey()).isEqualTo(anAccountDomainKey("account_domain_key"));
    }

    @Test
    public void preventsSavingAccountWithSameAccountNumber() {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.save(account);

        Account otherAccount = anAccount("1111", Currency.USD);
        assertThatThrownBy(() -> accountRepository.save(otherAccount))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessage("Account 1111 already exists");
    }

    @Test
    public void preventsSavingAlreadyPersistedAccount() {
        Account account = anAccount("1111", Currency.EUR);
        account.setDomainKey(anAccountDomainKey("existing_account_id"));

        assertThatThrownBy(() -> accountRepository.save(account))
                .isInstanceOf(AlreadyPersistedEntityException.class)
                .hasMessage("Account with key AccountDomainKey(accountId=existing_account_id) is already persisted");
    }

    @Test
    public void findsAccountByAccountNumber() {
        Account account = anAccount("1111", Currency.EUR);

        accountRepository.restoreRepositoryStateFrom(singletonList(account));

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(repoAccount.getAccountNumber()).isEqualTo(ACCOUNT_NUMBER);
    }

    @Test
    public void doesNotFindAccountByNonexistingAccountNumber() {
        Account account = anAccount("2222", Currency.EUR);

        accountRepository.restoreRepositoryStateFrom(singletonList(account));

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        assertThat(repoAccount).isNull();
    }

    @Test
    public void ensuresSafePublicationOnSaveOfResult() {
        Account account = anAccount("1111", Currency.EUR);
        Account savedAccount = accountRepository.save(account);
        savedAccount.credit(amountOf("100"));

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(repoAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
        assertThat(savedAccount.getBalance()).isEqualTo(amountOf("100"));
    }

    @Test
    public void ensuresSafePublicationOnSaveOfParameter() {
        Account account = anAccount("1111", Currency.EUR);

        accountRepository.save(account);
        account.credit(amountOf("100"));

        Account repoAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(repoAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
        assertThat(account.getBalance()).isEqualTo(amountOf("100"));
    }

    @Test
    public void ensuresSafePublicationOnFindByAccountNumber() {
        Account account = anAccount("1111", Currency.EUR);
        accountRepository.save(account);


        Account savedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);
        savedAccount.credit(amountOf("100"));

        Account retrievedAccount = accountRepository.findByAccountNumber(ACCOUNT_NUMBER);

        assertThat(retrievedAccount.getBalance()).isEqualTo(ZERO_AMOUNT);
        assertThat(savedAccount.getBalance()).isEqualTo(amountOf("100"));
    }

}