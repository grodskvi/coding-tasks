package taxcalculator.service;


import static org.assertj.core.api.Assertions.assertThat;
import static taxcalculator.domain.finance.Rate.aRateOf;
import static taxcalculator.domain.finance.Rates.*;
import static taxcalculator.prototypes.ItemPrototypes.someItem;

import org.junit.Before;
import org.junit.Test;

import taxcalculator.domain.Item;
import taxcalculator.domain.ItemCategory;
import taxcalculator.domain.ItemFeature;
import taxcalculator.domain.finance.Rate;
import taxcalculator.service.ImportDutyCalculator;

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
