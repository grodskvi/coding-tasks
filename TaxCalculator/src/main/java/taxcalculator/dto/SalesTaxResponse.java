package taxcalculator.dto;

import java.math.BigDecimal;

import taxcalculator.domain.finance.Money;

public class SalesTaxResponse implements Response {
	
	private BigDecimal salesTax;
	
	public SalesTaxResponse(Money salesTax) {
		this.salesTax = salesTax.getAmount();
	}

	public BigDecimal getSalesTax() {
		return salesTax;
	}
}
