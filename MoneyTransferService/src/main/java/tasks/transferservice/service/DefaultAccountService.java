package tasks.transferservice.service;

import static tasks.transferservice.domain.common.Currency.currencyOf;
import static tasks.transferservice.domain.entity.Account.anAccount;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.DepositRequest;
import tasks.transferservice.repository.AccountRepository;

@Service
public class DefaultAccountService implements AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAccountService.class);

    @Inject
    private AccountRepository accountRepository;

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
    public void deposit(String accountId, DepositRequest depositRequest) {

    }
}
