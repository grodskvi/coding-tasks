package taxcalulator.service;


import static org.assertj.core.api.Assertions.assertThat;
import static taxcalulator.domain.finance.Rates.*;
import static taxcalulator.domain.finance.Rate.aRateOf;
import static taxcalulator.prototypes.ItemPrototypes.someItem;

import org.junit.Before;
import org.junit.Test;

import taxcalulator.domain.Item;
import taxcalulator.domain.ItemCategory;
import taxcalulator.domain.ItemFeature;
import taxcalulator.domain.finance.Rate;

public class ImportDutyCalculatorTest {

	private ImportDutyCalculator taxRateCalculator;
	
	@Before
	public void setUp() {
		taxRateCalculator = new ImportDutyCalculator();
	}
	
	@Test
	public void returnsZeroTaxRateOnNullItem() {
		Rate taxRate = taxRateCalculator.calculateTaxRate(null);
		assertThat(taxRate).isEqualTo(ZERO_RATE);
	}
	
	@Test
	public void doesNotAppliesToNotImportedItems() {
		Item item = someItem().build();
		Rate taxRate = taxRateCalculator.calculateTaxRate(item);
		assertThat(taxRate).isEqualTo(ZERO_RATE);
	}
	
	@Test
	public void appliesFivePercentRateOnImportedItem() {
		Item item = someItem()
				.withCategory(ItemCategory.BOOK)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Rate taxRate = taxRateCalculator.calculateTaxRate(item);
		assertThat(taxRate).isEqualTo(Rate.aRateOf("0.05"));		
	}
}
