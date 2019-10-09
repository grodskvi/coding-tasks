package tasks.transferservice.domain.entity;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static tasks.transferservice.domain.common.AccountNumber.anAccountNumber;
import static tasks.transferservice.domain.common.AccountOperation.anAccountOperation;
import static tasks.transferservice.domain.common.AccountOperationType.CREDIT;
import static tasks.transferservice.domain.common.AccountOperationType.DEBIT;
import static tasks.transferservice.domain.common.Amount.ZERO_AMOUNT;
import static tasks.transferservice.domain.common.Amount.amountOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.ToString;
import tasks.transferservice.domain.common.AccountNumber;
import tasks.transferservice.domain.common.AccountOperation;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.common.Currency;
import tasks.transferservice.domain.exception.InsufficientFundsException;
import tasks.transferservice.validation.Preconditions;

@ToString
public class Account implements Cloneable {
    private AccountNumber accountNumber;
    private Currency accountCurrency;
    private Amount balance;
    private List<AccountOperation> accountStatement;

    private Account(AccountNumber accountNumber, Currency accountCurrency, Amount balance, List<AccountOperation> accountStatement) {
        this.accountNumber = accountNumber;
        this.accountCurrency = accountCurrency;
        this.balance = balance;
        this.accountStatement = new ArrayList<>(accountStatement); //single account instance should be never accessed from several threads
    }

    public Amount getBalance() {
        return balance;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public boolean hasAccountNumberValueOf(String accountNumber) {
        return this.getAccountNumber().getValue().equals(accountNumber);
    }

    public Currency getAccountCurrency() {
        return accountCurrency;
    }

    public Amount credit(Amount creditAmount) {
        Preconditions.checkNotNull(creditAmount, "Attempted to credit %s with null amount", accountNumber);

        balance = balance.increaseBy(creditAmount);
        accountStatement.add(anAccountOperation(CREDIT, creditAmount, accountCurrency));
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
        accountStatement.add(anAccountOperation(DEBIT, debitAmount, accountCurrency));
        return balance;
    }

    public List<AccountOperation> getAccountStatement() {
        return unmodifiableList(accountStatement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public Account clone() {
        return new Account(accountNumber, accountCurrency, amountOf(balance.getValue()), accountStatement);
    }

    public static Account anAccount(String accountNumber, Currency accountCurrency) {
        Preconditions.checkNotNull(accountNumber, "Can not create account without account number");
        Preconditions.checkNotNull(accountCurrency, "Can not create account without currency");

        return new Account(anAccountNumber(accountNumber), accountCurrency, ZERO_AMOUNT, emptyList());
    }
}
