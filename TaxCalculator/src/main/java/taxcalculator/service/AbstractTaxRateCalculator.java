package taxcalculator.service;

import static taxcalculator.domain.finance.Rates.ZERO_RATE;
import taxcalculator.domain.Item;
import taxcalculator.domain.finance.Rate;

public abstract class AbstractTaxRateCalculator implements TaxRateCalculator {

	@Override
	public Rate calculateTaxRate(Item item) {
		return item == null || !isApplicableItem(item) ?
				ZERO_RATE :
				doCalculateTaxRate(item);
	}
	
	protected abstract boolean isApplicableItem(Item item);
	
	protected abstract Rate doCalculateTaxRate(Item item);
}
