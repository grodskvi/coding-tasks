package taxcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import taxcalculator.domain.Quantity;
import taxcalculator.exception.InvalidDataException;

public class QuantityTest {

	@Test
	public void createsSpecifiedQuantity() {
		Quantity quantity = new Quantity(0);
		assertThat(quantity.getQuantity()).isEqualTo(0);
	}
	
	@Test
	public void failsToCreateNegativeQuantity() {
		assertThatThrownBy(() -> new Quantity(-1)).isInstanceOf(InvalidDataException.class);
	}

}
