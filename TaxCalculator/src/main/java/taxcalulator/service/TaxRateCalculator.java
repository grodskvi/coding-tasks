package taxcalulator.service;

import taxcalulator.domain.Item;
import taxcalulator.domain.finance.Rate;

public interface TaxRateCalculator {
	Rate calculateTaxRate(Item item);
}
