package taxcalculator.domain;

import static taxcalculator.utils.ParametersValidator.validateParameter;

public class ItemQuantity {
	
	private int quantity;

	public ItemQuantity(int quantity) {
		validateParameter(quantity, i -> i >= 0, "Quantity can not be negative");
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
}
