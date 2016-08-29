package taxcalculator.domain.finance;

import static taxcalculator.utils.ParametersValidator.NON_NEGATIVE_NUMBER;
import static taxcalculator.utils.ParametersValidator.validateParameter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import taxcalculator.domain.Precision;

import com.google.common.base.Preconditions;

public class Money {
	
	private static final Precision DEFAULT_PRECISION = Precision.precision("0.01");
	
	public static final Money ZERO_AMOUNT = new Money(BigDecimal.ZERO);
	
	private BigDecimal amount;

	public Money(BigDecimal amount) {
		validateParameter(amount, NON_NEGATIVE_NUMBER, "Can not create Money with negative amount");
		this.amount = round(amount, DEFAULT_PRECISION);
	}
	
	public Money(Money money, Precision precision) {
		Preconditions.checkNotNull(money, "Money parameter can not be null");
		Preconditions.checkNotNull(precision, "Precision parameter can not be null");
		amount = round(money.amount, precision);
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public Money applyRate(Rate taxRate) {
		Preconditions.checkNotNull(taxRate, "Can not apply null rate");
		BigDecimal ratedAmount = amount.multiply(taxRate.getRate());
		return new Money(ratedAmount);
	}
	
	public Money sum(Money money) {
		BigDecimal totalAmount = amount.add(money.amount);
		return new Money(totalAmount);
	}
	
	public static Money money(String amount) {
		return new Money(new BigDecimal(amount));
	}
	
	private BigDecimal round(BigDecimal amount, Precision precision) {
		BigDecimal scale = BigDecimal.ONE.divide(precision.getPrecision());
		BigDecimal roundedAmount = amount.multiply(scale).setScale(0, RoundingMode.UP).divide(scale);
		return roundedAmount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (amount.compareTo(other.amount) != 0) 
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Money [amount=" + amount + "]";
	}
}
