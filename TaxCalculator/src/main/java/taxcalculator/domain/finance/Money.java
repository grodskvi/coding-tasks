package taxcalculator.domain.finance;

import static taxcalculator.utils.ParametersValidator.NON_NEGATIVE_NUMBER;
import static taxcalculator.utils.ParametersValidator.validateParameter;

import java.math.BigDecimal;
import java.util.Objects;

import com.google.common.base.Preconditions;

public class Money {
	
	public static final Money ZERO_AMOUNT = new Money(BigDecimal.ZERO);
	
	private BigDecimal amount;

	public Money(BigDecimal amount) {
		validateParameter(amount, NON_NEGATIVE_NUMBER, "Can not create Money with negative amount");
		this.amount = amount;
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
