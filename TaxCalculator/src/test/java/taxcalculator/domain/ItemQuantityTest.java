package taxcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import taxcalculator.domain.ItemQuantity;
import taxcalculator.exception.InvalidDataException;

public class ItemQuantityTest {

	@Test
	public void createsMoneyWithSpecifiedAmount() {
		ItemQuantity quantity = new ItemQuantity(0);
		assertThat(quantity.getQuantity()).isEqualTo(0);
	}
	
	@Test
	public void failsToCreateMoneyWithNegativeAmount() {
		assertThatThrownBy(() -> new ItemQuantity(-1)).isInstanceOf(InvalidDataException.class);
	}

}
