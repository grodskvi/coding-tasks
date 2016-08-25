package taxcalulator.domain.finance;

import static taxcalulator.utils.ParametersValidator.NON_NEGATIVE_NUMBER;
import static taxcalulator.utils.ParametersValidator.validateParameter;

import java.math.BigDecimal;
import java.util.Objects;

public class Rate {

	private BigDecimal rate;

	public Rate(BigDecimal rate) {
		validateParameter(rate, NON_NEGATIVE_NUMBER, "Can not create Rate with negative rate");
		this.rate = rate;
	}

	BigDecimal getRate() {
		return rate;
	}
	
	public Rate sum(Rate anotherRate) {
		BigDecimal totalRate = rate.add(anotherRate.rate);
		return new Rate(totalRate);
	}
	
	public static Rate aRateOf(String rate) {
		return new Rate(new BigDecimal(rate));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(rate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rate other = (Rate) obj;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (rate.compareTo(other.rate) != 0)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rate [rate=" + rate + "]";
	}

}
