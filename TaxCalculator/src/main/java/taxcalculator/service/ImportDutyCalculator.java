package taxcalculator.service;

import static taxcalculator.domain.finance.Rates.FIVE_PERCENT_RATE;
import taxcalculator.domain.Item;
import taxcalculator.domain.finance.Rate;

public class ImportDutyCalculator extends AbstractTaxRateCalculator {

	@Override
	protected boolean isApplicableItem(Item item) {
		return item.isImported();
	}

	@Override
	protected Rate doCalculateTaxRate(Item item) {
		return FIVE_PERCENT_RATE;
	}

}
