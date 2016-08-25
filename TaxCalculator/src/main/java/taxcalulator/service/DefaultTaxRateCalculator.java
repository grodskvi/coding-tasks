package taxcalulator.service;

import static taxcalulator.domain.ItemCategory.BOOK;
import static taxcalulator.domain.ItemCategory.FOOD;
import static taxcalulator.domain.ItemCategory.MEDICINE;
import static taxcalulator.domain.finance.Rates.TEN_PERCENT_RATE;

import java.util.EnumSet;
import java.util.Set;

import taxcalulator.domain.Item;
import taxcalulator.domain.ItemCategory;
import taxcalulator.domain.finance.Rate;

public class DefaultTaxRateCalculator extends AbstractTaxRateCalculator {
	
	private static final Set<ItemCategory> exemptionCategories = EnumSet.of(BOOK, FOOD, MEDICINE);

	@Override
	protected boolean isApplicableItem(Item item) {
		return !exemptionCategories.contains(item.getCategory());
	}

	@Override
	protected Rate doCalculateTaxRate(Item item) {
		return TEN_PERCENT_RATE;
	}
}
