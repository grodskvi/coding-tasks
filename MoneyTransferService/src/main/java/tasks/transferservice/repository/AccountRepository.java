package tasks.transferservice.repository;

import org.jvnet.hk2.annotations.Contract;

import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.entity.Account;
import tasks.transferservice.domain.exception.DuplicateAccountException;

@Contract
public interface AccountRepository {

    Account save(Account account) throws DuplicateAccountException;

    Account update(Account account);

    Account findByAccountNumber(AccountNumber accountNumber);

    Account lockForUpdate(AccountNumber accountNumber);

    void unlock(Account account);
}
