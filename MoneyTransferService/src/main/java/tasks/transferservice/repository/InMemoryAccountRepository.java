package tasks.transferservice.repository;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static tasks.transferservice.repository.PersistedEntity.persistedEntity;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.repository.exception.DuplicateEntityException;
import tasks.transferservice.repository.exception.EntityNotFoundException;

@Service
public class InMemoryAccountRepository implements AccountRepository {

    private static Logger LOG = LoggerFactory.getLogger(InMemoryAccountRepository.class);

    private static final Function<Account, Account> ACCOUNT_PERSISTENCE_VIEW = Account::clone;

    private ConcurrentHashMap<AccountNumber, PersistedEntity<Account>> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        LOG.debug("Saving account {}", account);
        AccountNumber accountNumber = account.getAccountNumber();
        PersistedEntity<Account> persistedAccount = persistedEntity(account, ACCOUNT_PERSISTENCE_VIEW);

        PersistedEntity<Account> existingAccount =
                accounts.putIfAbsent(accountNumber, persistedAccount);
        if (existingAccount != null) {
            LOG.info("Account {} already exists: {}", accountNumber, account);
            throw new DuplicateEntityException(format("Account %s already exists", accountNumber.getValue()));
        }
        return persistedAccount.getEntity();
    }

    @Override
    public Account update(Account account) {
        LOG.debug("Updating account {}", account);

        PersistedEntity<Account> updatedAccount = accounts.computeIfPresent(
            account.getAccountNumber(),
            (accountNumber, existingAccount) -> existingAccount.updateWith(account));

        if (updatedAccount == null) {
            LOG.info("Attempting to update non-existing account {}", account.getAccountNumber());
            throw new EntityNotFoundException(account.toEntityKey());
        }

        return updatedAccount.getEntity();
    }

    @Override
    public Account findByAccountNumber(AccountNumber accountNumber) {
        PersistedEntity<Account> persistedAccount = accounts.get(accountNumber);
        return persistedAccount != null ? persistedAccount.getEntity() : null;
    }

    @Override
    public Account lockForUpdate(AccountNumber accountNumber) {
        PersistedEntity<Account> account = accounts.get(accountNumber);
        if (account == null) {
            LOG.warn("Account {} is not found. Nothing to lock", account);
            throw new EntityNotFoundException(Account.toEntityKey(accountNumber));
        }
        account.lockForUpdate();
        return accounts.get(accountNumber).getEntity();
    }

    @Override
    public void unlock(Account account) {
        LOG.debug("Unlocking account {}", account.getAccountNumber());
        PersistedEntity<Account> persistedEntity = accounts.get(account.getAccountNumber());
        if (persistedEntity == null) {
            LOG.warn("Account {} is not found. Nothing to unlock", account);
            throw new EntityNotFoundException(account.toEntityKey());
        }
        persistedEntity.unlock();
    }

    // currently used for test setup -- therefore no need in concurrency control for now
    void restoreRepositoryStateFrom(Account... savedAccounts) {
        Map<AccountNumber, PersistedEntity<Account>> accounts = Arrays.stream(savedAccounts)
            .collect(toMap(Account::getAccountNumber, account -> persistedEntity(account, ACCOUNT_PERSISTENCE_VIEW)));

        this.accounts.clear();
        this.accounts.putAll(accounts);
    }
}
