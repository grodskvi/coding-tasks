package tasks.transferservice.service;

import org.jvnet.hk2.annotations.Contract;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.rest.CreateAccountRequest;
import tasks.transferservice.domain.rest.DepositRequest;

@Contract
public interface AccountService {
    Account createAccount(CreateAccountRequest createAccountRequest);

    void deposit(AccountNumber accountId, DepositRequest depositRequest);
}
