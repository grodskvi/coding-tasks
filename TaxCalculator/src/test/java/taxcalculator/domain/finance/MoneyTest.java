package taxcalculator.domain.finance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static taxcalculator.domain.finance.Money.money;
import static taxcalculator.domain.finance.Rate.aRateOf;

import java.math.BigDecimal;

import org.junit.Test;

import taxcalculator.domain.Precision;
import taxcalculator.exception.InvalidDataException;

public class MoneyTest {

	@Test
	public void createsMoneyWithSpecifiedAmount() {
		Money money = new Money(BigDecimal.ZERO);
		assertThat(money.getAmount()).isZero();
	}
	
	@Test
	public void failsToCreateMoneyWithNegativeAmount() {
		BigDecimal negativeAmount = new BigDecimal(-1);
		assertThatThrownBy(() -> new Money(negativeAmount)).isInstanceOf(InvalidDataException.class);
	}
	
	@Test
	public void failsToCreateMoneyWithNullAmount() {
		assertThatThrownBy(() -> new Money(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Parameter is null");
	}
	
	@Test
	public void failsToCreateRoundedMoneyWithNullAmount() {
		assertThatThrownBy(() -> new Money(null, Precision.precision("1")))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Money parameter can not be null");
	}
	
	@Test
	public void failsToCreateRoundedMoneyWithNullPrecision() {
		assertThatThrownBy(() -> new Money(Money.ZERO_AMOUNT, null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Precision parameter can not be null");
	}
	
	@Test
	public void appliesRate() {
		Money money = money("100");
		Rate rate = aRateOf("0.3");
		assertThat(money.applyRate(rate)).isEqualTo(money("30.0"));
	}
	
	@Test
	public void roundsAmountUpByDefaultToSecondFraction() {
		Money money = money("100.1103");
		assertThat(money).isEqualTo(money("100.12"));
	}
	
	@Test
	public void roundsMoneyWithSpecifiedPrecision() {
		Money money = money("10.56");
		assertThat(new Money(money, Precision.precision("0.05"))).isEqualTo(money("10.6"));
	}

}
