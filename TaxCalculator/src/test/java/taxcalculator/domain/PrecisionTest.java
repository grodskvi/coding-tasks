package taxcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class PrecisionTest {

	@Test
	public void createsSpecifiedPrecision() {
		Precision precision = new Precision(BigDecimal.ONE);
		assertThat(precision.getPrecision()).isEqualTo(BigDecimal.ONE);
	}
	
	@Test
	public void failsToCreateNegativePrecision() {
		assertThatThrownBy(() -> new Precision.precision("-0.1")))).isInstanceOf(InvalidDataException.class);
	}

}
