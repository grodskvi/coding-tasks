package tasks.transferservice.domain.entity;

import org.junit.Test;
import tasks.transferservice.domain.common.Amount;
import tasks.transferservice.domain.exception.InsufficientFundsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.Amount.amountOf;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.entity.Account.anAccount;

public class AccountTest {

    private static final String ACCOUNT_NUMBER = "111-222-333";

    @Test
    public void initializesAccountWithZeroBalance() {
        Account account = anAccount(ACCOUNT_NUMBER, EUR);

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
        Account account = anAccount(ACCOUNT_NUMBER, EUR);
        Amount creditAmount = amountOf("34.08");

        account.credit(creditAmount);

        assertThat(account.getBalance()).isEqualTo(creditAmount);
    }

    @Test
    public void updatesAccountBalanceOnSequentialCredits() {
        Account account = anAccount(ACCOUNT_NUMBER, EUR);
        Amount creditAmount = amountOf("34.08");
        Amount nextCreditAmount = amountOf("15");

        account.credit(creditAmount);
        account.credit(nextCreditAmount);

        assertThat(account.getBalance()).isEqualTo(amountOf("49.08"));
    }

    @Test
    public void updatesAccountBalanceOnDebit() throws InsufficientFundsException {
        Account account = anAccount(ACCOUNT_NUMBER, EUR);
        Amount creditAmount = amountOf("34.08");
        Amount debitAmount = amountOf("15");

        account.credit(creditAmount);
        account.debit(debitAmount);

        assertThat(account.getBalance()).isEqualTo(amountOf("19.08"));
    }

    @Test
    public void failsOnDebettingAmountMoreThenAvalableBalance() throws InsufficientFundsException {
        Account account = anAccount(ACCOUNT_NUMBER, EUR);
        Amount creditAmount = amountOf("34.08");
        Amount debitAmount = amountOf("50.00");

        account.credit(creditAmount);
        assertThatThrownBy(() -> account.debit(debitAmount))
                .isInstanceOf(InsufficientFundsException.class);

        assertThat(account.getBalance()).isEqualTo(creditAmount);
    }

}