package tasks.transferservice.domain.exception;

import static java.lang.String.format;

import tasks.transferservice.domain.common.AccountNumber;

public class DuplicateAccountException extends Exception {
    private AccountNumber accountNumber;

    public DuplicateAccountException(AccountNumber accountNumber) {
        super(format("Account %s already exists", accountNumber.getValue()));
        this.accountNumber = accountNumber;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }
}
