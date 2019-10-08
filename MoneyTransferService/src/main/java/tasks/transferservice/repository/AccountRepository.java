package tasks.transferservice.repository;

import org.jvnet.hk2.annotations.Contract;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;

@Contract
public interface AccountRepository {

    Account save(Account account);

    Account update(Account account);

    Account findByAccountNumber(AccountNumber accountNumber);
}
