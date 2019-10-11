package tasks.transferservice.domain.exception;

import static java.lang.String.format;

import tasks.transferservice.domain.common.AccountNumber;

public class AccountNotFoundException extends Exception {
    private AccountNumber accountNumber;

    public AccountNotFoundException(AccountNumber accountNumber) {
        super(format("Account '%s' does not exist", accountNumber.getValue()));
        this.accountNumber = accountNumber;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }
}
