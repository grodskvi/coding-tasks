package tasks.transferservice.repository;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static tasks.transferservice.domain.common.AccountDomainKey.anAccountDomainKey;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.transferservice.domain.common.AccountDomainKey;
import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.repository.exception.AlreadyPersistedEntityException;
import tasks.transferservice.repository.exception.DuplicateEntityException;
import tasks.transferservice.service.DomainKeyProvider;

@Service
public class InMemoryAccountRepository implements AccountRepository {

    private static Logger LOG = LoggerFactory.getLogger(InMemoryAccountRepository.class);

    @Inject
    private DomainKeyProvider domainKeyProvider;
    private ConcurrentHashMap<AccountNumber, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        LOG.debug("Saving account {}", account);
        if (account.getDomainKey() != null) {
            LOG.info("Attempted to save instance of already persisted entity with key {}: ", account.getDomainKey(), account);
            throw new AlreadyPersistedEntityException(format("Account with key %s is already persisted", account.getDomainKey()));
        }
        return doSave(account.clone());
    }

    private Account doSave(Account account) {
        AccountDomainKey key = anAccountDomainKey(domainKeyProvider.nextDomainKey());
        account.setDomainKey(key);

        AccountNumber accountNumber = account.getAccountNumber();
        Account existingAccount = accounts.putIfAbsent(accountNumber, account);
        if (existingAccount != null) {
            LOG.info("Account {} already exists: {}", accountNumber, account);
            throw new DuplicateEntityException(format("Account %s already exists", accountNumber.getAccountNumber()));
        }
        return account.clone();
    }

    @Override
    public Account findByAccountNumber(AccountNumber accountNumber) {
        Account repoAccount = accounts.get(accountNumber);
        return repoAccount != null ? repoAccount.clone() : null;
    }

    // currently used for test setup -- therefore no need in concurrency control for now
    void restoreRepositoryStateFrom(List<Account> savedAccounts) {
        Map<AccountNumber, Account> accounts = savedAccounts.stream()
            .collect(toMap(Account::getAccountNumber, Account::clone));

        this.accounts.clear();
        this.accounts.putAll(accounts);
    }
}
