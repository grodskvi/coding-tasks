package taxcalculator.domain.finance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static taxcalculator.domain.finance.Rate.aRateOf;

import java.math.BigDecimal;

import org.junit.Test;

import taxcalculator.domain.finance.Money;
import taxcalculator.domain.finance.Rate;
import taxcalculator.exception.InvalidDataException;

public class RateTest {

	@Test
	public void createsRateWithSpecifiedAmount() {
		Rate rate = new Rate(BigDecimal.ZERO);
		assertThat(rate.getRate()).isEqualTo(BigDecimal.ZERO);
	}
	
	@Test
	public void failsToCreateRateWithNegativeRate() {
		BigDecimal negativeRate = new BigDecimal(-1);
		assertThatThrownBy(() -> new Money(negativeRate)).isInstanceOf(InvalidDataException.class);
	}
	
	@Test
	public void failsToCreateNullableRate() {
		assertThatThrownBy(() -> new Rate(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("Parameter is null");
	}
	
	@Test
	public void combinesRates() {
		Rate rate = aRateOf("0.02");
		Rate anotherRate = aRateOf("0.08");
		
		assertThat(rate.sum(anotherRate)).isEqualTo(aRateOf("0.10"));
	}

}
