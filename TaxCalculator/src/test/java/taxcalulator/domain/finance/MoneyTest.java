package taxcalulator.domain.finance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.Test;

import taxcalulator.exception.InvalidDataException;

public class MoneyTest {

	@Test
	public void createsMoneyWithSpecifiedAmount() {
		Money money = new Money(BigDecimal.ZERO);
		assertThat(money.getAmount()).isEqualTo(BigDecimal.ZERO);
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
	public void appliesRate() {
		Money money = Money.money("100");
		Rate rate = Rate.aRateOf("0.3");
		assertThat(money.applyRate(rate)).isEqualTo(Money.money("30.0"));
	}

}
