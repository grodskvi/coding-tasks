package taxcalculator.service;

import static taxcalculator.domain.ItemCategory.BOOK;
import static taxcalculator.domain.ItemCategory.FOOD;
import static taxcalculator.domain.ItemCategory.MEDICINE;
import static taxcalculator.domain.finance.Rates.TEN_PERCENT_RATE;

import java.util.EnumSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import taxcalculator.domain.Item;
import taxcalculator.domain.ItemCategory;
import taxcalculator.domain.finance.Rate;

@Component
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
