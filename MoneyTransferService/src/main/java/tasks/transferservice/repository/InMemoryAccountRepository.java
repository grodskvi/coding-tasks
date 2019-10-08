package tasks.transferservice.repository;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private ConcurrentHashMap<AccountNumber, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        LOG.debug("Saving account {}", account);
        return doSave(account.clone());
    }

    @Override
    public Account update(Account account) {
        LOG.debug("Updating account {}", account);

        Account copiedAccount = account.clone();
        Account updatedAccount = accounts.computeIfPresent(
            account.getAccountNumber(),
            (accountNumber, existingAccount) -> copiedAccount);

        if (updatedAccount == null) {
            LOG.info("Attempting to update non-existing account {}", account.getAccountNumber());
            throw new EntityNotFoundException(account.getAccountNumber().getValue(), Account.class);
        }

        return copiedAccount.clone();
    }

    private Account doSave(Account account) {
        AccountNumber accountNumber = account.getAccountNumber();
        Account existingAccount = accounts.putIfAbsent(accountNumber, account);
        if (existingAccount != null) {
            LOG.info("Account {} already exists: {}", accountNumber, account);
            throw new DuplicateEntityException(format("Account %s already exists", accountNumber.getValue()));
        }
        return account.clone();
    }

    @Override
    public Account findByAccountNumber(AccountNumber accountNumber) {
        Account repoAccount = accounts.get(accountNumber);
        return repoAccount != null ? repoAccount.clone() : null;
    }

    // currently used for test setup -- therefore no need in concurrency control for now
    void restoreRepositoryStateFrom(Account... savedAccounts) {
        Map<AccountNumber, Account> accounts = Arrays.stream(savedAccounts)
            .collect(toMap(Account::getAccountNumber, Account::clone));

        this.accounts.clear();
        this.accounts.putAll(accounts);
    }
}
