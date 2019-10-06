package tasks.transferservice.domain.common;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.Amount.amountOf;

public class AmountTest {

    @Test
    public void createsAssetsWithPositiveAmount() {
        Amount amount = amountOf(BigDecimal.TEN);

        assertThat(amount)
                .extracting("value")
                .isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void createsAssetsWithZeroAmount() {
        Amount amount = amountOf(BigDecimal.ZERO);

        assertThat(amount)
                .extracting("value")
                .isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void failsToCreateUndefinedAmount() {
        assertThatThrownBy(() -> amountOf((String)null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create amount with value 'null'");
    }

    @Test
    public void failsToCreateNegativeAmount() {
        assertThatThrownBy(() -> amountOf(BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create amount with value '-10'");
    }

    @Test
    public void failsToCreateAssetsNonNumericAmount() {
        assertThatThrownBy(() -> amountOf("1a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not create amount with value '1a'");
    }

    @Test
    public void increasesAsset() {
        Amount initialAmount = amountOf("55.04");
        Amount credit = amountOf("37.33");
        Amount updatedAmount = amountOf("92.37");

        assertThat(initialAmount.increaseBy(credit)).isEqualTo(updatedAmount);
    }

    @Test
    public void failsToIncreaseWithNullCredit() {
        Amount initialAmount = amountOf("55.04");

        assertThatThrownBy(() -> initialAmount.increaseBy(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attempted to increase '55.04' amount with null");
    }

    @Test
    public void decreasesAsset() {
        Amount initialAmount = amountOf("55.04");
        Amount debit = amountOf("37.33");
        Amount updatedAmount = amountOf("17.71");

        assertThat(initialAmount.decreaseBy(debit))
                .isEqualTo(updatedAmount);
    }

    @Test
    public void failsToDecreaseWithNullDebit() {
        Amount initialAmount = amountOf("55.04");

        assertThatThrownBy(() -> initialAmount.decreaseBy(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attempted to decrease '55.04' amount with null");
    }

    @Test
    public void failsToDecreaseWithDebitBiggerThenValue() {
        Amount initialAmount = amountOf("55.04");
        Amount debit = amountOf("100.00");

        assertThatThrownBy(() -> initialAmount.decreaseBy(debit))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attempted to decrease '55.04' by '100.00'");
    }

    @Test
    public void indicatesWhetherAmountIsLessThenGivenAmount() {
        Amount etalon = amountOf("1");

        assertThat(etalon.isLessThen(amountOf("1.001"))).isTrue();
        assertThat(etalon.isLessThen(amountOf("1"))).isFalse();
        assertThat(etalon.isLessThen(amountOf("1.000"))).isFalse();
        assertThat(etalon.isLessThen(amountOf("0.999"))).isFalse();
    }
}