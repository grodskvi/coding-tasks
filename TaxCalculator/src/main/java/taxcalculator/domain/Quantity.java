package taxcalculator.domain;

import static taxcalculator.utils.ParametersValidator.validateParameter;

public class Quantity {
	
	private int quantity;

	public Quantity(int quantity) {
		validateParameter(quantity, i -> i >= 0, "Quantity can not be negative");
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + quantity;
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
		Quantity other = (Quantity) obj;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemQuantity [quantity=" + quantity + "]";
	}

	public static Quantity quantity(int quantity) {
		return new Quantity(quantity);
	}
}
