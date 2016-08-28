package taxcalculator.service;

import taxcalculator.domain.Item;
import taxcalculator.domain.finance.Rate;

public interface TaxRateCalculator {
	Rate calculateTaxRate(Item item);
}
