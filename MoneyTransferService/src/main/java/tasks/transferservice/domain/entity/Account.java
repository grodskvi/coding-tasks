package tasks.transferservice.domain.entity;

import lombok.ToString;
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
import static tasks.transferservice.domain.common.Amount.amountOf;

@ToString
public class Account implements Cloneable {
    private AccountDomainKey domainKey;
    private AccountNumber accountNumber;
    private Currency accountCurrency;
    private Amount balance;

    private Account(AccountNumber accountNumber, Currency accountCurrency, Amount balance) {
        this.accountNumber = accountNumber;
        this.accountCurrency = accountCurrency;
        this.balance = balance;
    }

    public AccountDomainKey getDomainKey() {
        return domainKey;
    }

    public void setDomainKey(AccountDomainKey domainKey) {
        this.domainKey = domainKey;
    }

    public Amount getBalance() {
        return balance;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
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

    @Override
    public Account clone() {
        Account account = new Account(accountNumber, accountCurrency, amountOf(balance.getValue()));
        account.domainKey = domainKey;

        return account;
    }

    public static Account anAccount(String accountNumber, Currency accountCurrency) {
        Preconditions.checkNotNull(accountNumber, "Can not create account without account number");
        Preconditions.checkNotNull(accountCurrency, "Can not create account without currency");

        return new Account(anAccountNumber(accountNumber), accountCurrency, ZERO_AMOUNT);
    }
}
