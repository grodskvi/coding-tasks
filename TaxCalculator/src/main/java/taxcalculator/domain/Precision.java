package taxcalculator.domain;

import java.math.BigDecimal;

import taxcalculator.utils.ParametersValidator;

public class Precision {
	
	private BigDecimal precision;
	
	public Precision(BigDecimal precision) {
		ParametersValidator.validateParameter(precision, ParametersValidator.NON_NEGATIVE_NUMBER, "Precision should be non negative");
		this.precision = precision;
	}
	
	public BigDecimal getPrecision() {
		return precision;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((precision == null) ? 0 : precision.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Precision other = (Precision) obj;
		if (precision == null) {
			if (other.precision != null)
				return false;
		} else if (precision.compareTo(other.precision) != 0)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Precision [precision=" + precision + "]";
	}

	public static Precision precision(String precision) {
		return new Precision(new BigDecimal(precision));
	}

}
