package tasks.transferservice.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.AccountOperation.anAccountOperation;
import static tasks.transferservice.domain.common.AccountOperationType.CREDIT;
import static tasks.transferservice.domain.common.AccountOperationType.DEBIT;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.entity.Account.anAccount;

import org.junit.Test;

import tasks.transferservice.domain.common.AccountOperation;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.exception.InsufficientFundsException;

public class AccountTest {

    private static final String ACCOUNT_NUMBER = "111-222-333";

    private Account account = anAccount(ACCOUNT_NUMBER, EUR);

    @Test
    public void initializesAccountWithZeroBalance() {
        assertThat(account.getBalance()).isEqualTo(Amount.ZERO_AMOUNT);
    }

    @Test
    public void failsToCreateAccountWihNullAccountNumber() {
        assertThatThrownBy(() -> anAccount(null, EUR))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create account without account number");
    }

    @Test
    public void failsToCreateAccountWihNullCurrency() {
        assertThatThrownBy(() -> anAccount(ACCOUNT_NUMBER, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create account without currency");
    }

    @Test
    public void updatesAccountBalanceOnCredit() {
        Amount creditAmount = amountOf("34.08");

        account.credit(creditAmount);

        assertThat(account.getBalance()).isEqualTo(creditAmount);
    }

    @Test
    public void updatesAccountBalanceOnSequentialCredits() {
        Amount creditAmount = amountOf("34.08");
        Amount nextCreditAmount = amountOf("15");

        account.credit(creditAmount);
        account.credit(nextCreditAmount);

        assertThat(account.getBalance()).isEqualTo(amountOf("49.08"));
    }

    @Test
    public void updatesAccountBalanceOnDebit() throws InsufficientFundsException {
        Amount creditAmount = amountOf("34.08");
        Amount debitAmount = amountOf("15");

        account.credit(creditAmount);
        account.debit(debitAmount);

        assertThat(account.getBalance()).isEqualTo(amountOf("19.08"));
    }

    @Test
    public void failsOnDebettingAmountMoreThenAvailableBalance() {
        Amount creditAmount = amountOf("34.08");
        Amount debitAmount = amountOf("50.00");

        account.credit(creditAmount);
        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InsufficientFundsException.class);

        assertThat(account.getBalance()).isEqualTo(creditAmount);
    }

    @Test
    public void recordsOperationOnCredit() {
        Amount creditAmount = amountOf("34.08");
        account.credit(creditAmount);

        AccountOperation expectedStatementItem = anAccountOperation(CREDIT, creditAmount, EUR);
        assertThat(account.getAccountStatement()).containsExactly(expectedStatementItem);
    }

    @Test
    public void recordsOperationOnDebit() throws InsufficientFundsException {
        Amount creditAmount = amountOf("34.08");
        Amount debitAmount = amountOf("17");

        account.credit(creditAmount);
        account.debit(debitAmount);

        assertThat(account.getAccountStatement()).containsExactly(
            anAccountOperation(CREDIT, creditAmount, EUR),
            anAccountOperation(DEBIT, debitAmount, EUR));
    }

    @Test
    public void clonesAllFields() throws InsufficientFundsException {
        Account account = anAccount("111", EUR);
        account.credit(amountOf("100"));
        account.debit(amountOf("30"));

        assertThat(account.clone()).isEqualToComparingFieldByField(account);
    }

}