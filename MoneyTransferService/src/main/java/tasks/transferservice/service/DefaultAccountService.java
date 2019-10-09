package tasks.transferservice.service;

import static tasks.transferservice.domain.common.Currency.currencyOf;
import static tasks.transferservice.domain.entity.Account.anAccount;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.repository.AccountRepository;
import tasks.transferservice.repository.exception.EntityNotFoundException;

@Service
public class DefaultAccountService implements AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAccountService.class);

    private AccountRepository accountRepository;

    @Inject
    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        LOG.debug("Creating account according to request {}", createAccountRequest);

        Account account = anAccount(
            createAccountRequest.getAccountNumber(),
            currencyOf(createAccountRequest.getCurrencyIsoCode()));

        Account savedAccount = accountRepository.save(account);

        LOG.info("Created account {}", savedAccount);
        return savedAccount;
    }

    @Override
    public void deposit(AccountNumber accountNumber, DepositRequest depositRequest) {
        LOG.debug("Handling {} for account {}", depositRequest, accountNumber);
        Account account = null;
        try {
            account = accountRepository.lockForUpdate(accountNumber);
            if (account == null) {
                LOG.info("Account {} does not exist. Can't complete {}", accountNumber, depositRequest);
                throw new EntityNotFoundException(accountNumber.getValue(), Account.class);
            }

            Amount depositAmount = Amount.amountOf(depositRequest.getAmount());
            account.credit(depositAmount);

            Account updatedAccount = accountRepository.update(account);
            LOG.info("Updated account {} according to {}", updatedAccount, depositRequest);
        } finally {
            accountRepository.unlock(account);
        }
    }

    @Override
    public Account getAccount(AccountNumber accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            LOG.info("Can not find account by {}", accountNumber);
            throw new EntityNotFoundException(accountNumber.getValue(), Account.class);
        }
        return account;
    }
}
