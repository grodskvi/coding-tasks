package tasks.transferservice.domain.entity;

import tasks.transferservice.domain.common.AccountDomainKey;
import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.common.Currency;
import tasks.transferservice.domain.exception.InsufficientFundsException;
import tasks.transferservice.validation.Preconditions;

import java.util.Objects;

import static java.lang.String.format;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.Amount.ZERO_AMOUNT;

public class Account {
    private AccountDomainKey domainKey;
    private AccountNumber accountNumber;
    private Currency accountCurrency;
    private Amount balance;

    public Account(AccountNumber accountNumber, Currency accountCurrency, Amount balance) {
        this.accountNumber = accountNumber;
        this.accountCurrency = accountCurrency;
        this.balance = balance;
    }

    public Amount getBalance() {
        return balance;
    }

    public Amount credit(Amount creditAmount) {
        Preconditions.checkNotNull(creditAmount, "Attempted to credit %s with null amount", accountNumber);

        balance = balance.increaseBy(creditAmount);
        return balance;
    }

    public Amount debit(Amount debitAmount) throws InsufficientFundsException {
        Preconditions.checkNotNull(debitAmount, "Attempted to debit %s with null amount", accountNumber);

        if (balance.isLessThen(debitAmount)) {
            String message = format("Can not deposit account %s on %s amount. Available balance is %s",
                    accountNumber, debitAmount, balance);

            throw new InsufficientFundsException(message);
        }

        balance = balance.decreaseBy(debitAmount);
        return balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(domainKey, account.domainKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainKey);
    }

    public static Account anAccount(String accountNumber, Currency accountCurrency) {
        Preconditions.checkNotNull(accountNumber, "Can not create account without account number");
        Preconditions.checkNotNull(accountCurrency, "Can not create account without currency");

        return new Account(anAccountNumber(accountNumber), accountCurrency, ZERO_AMOUNT);
    }
}
