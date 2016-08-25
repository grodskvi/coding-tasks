package taxcalulator.service;

import static taxcalulator.domain.finance.Rates.FIVE_PERCENT_RATE;
import taxcalulator.domain.Item;
import taxcalulator.domain.finance.Rate;

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
