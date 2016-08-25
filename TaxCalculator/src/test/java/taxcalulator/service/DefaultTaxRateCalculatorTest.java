package taxcalulator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static taxcalulator.domain.finance.Rate.aRateOf;
import static taxcalulator.domain.finance.Rates.ZERO_RATE;
import static taxcalulator.prototypes.ItemPrototypes.someItem;

import org.junit.Before;
import org.junit.Test;

import taxcalulator.domain.Item;
import taxcalulator.domain.ItemCategory;
import taxcalulator.domain.finance.Rate;

public class DefaultTaxRateCalculatorTest {
	
	private DefaultTaxRateCalculator taxRateCalculator;
	
	@Before
	public void setUp() {
		taxRateCalculator = new DefaultTaxRateCalculator();
	}

	@Test
	public void returnsZeroTaxRateOnNullItem() {
		Rate taxRate = taxRateCalculator.calculateTaxRate(null);
		assertThat(taxRate).isEqualTo(ZERO_RATE);
	}
	
	@Test
	public void returnsTenPercentTaxRateOnItem() {
		Item item = someItem().build();
		Rate taxRate = taxRateCalculator.calculateTaxRate(item);
		assertThat(taxRate).isEqualTo(aRateOf("0.1"));
	}
	
	@Test
	public void treatsBooksAsTaxExemption() {
		Item item = aBook();
		Rate taxRate = taxRateCalculator.calculateTaxRate(item);
		assertThat(taxRate).isEqualTo(ZERO_RATE);
	}
	
	@Test
	public void treatsFoodAsTaxExemption() {
		Item item = aFood();
		Rate taxRate = taxRateCalculator.calculateTaxRate(item);
		assertThat(taxRate).isEqualTo(ZERO_RATE);
	}
	
	@Test
	public void treatsMedicineAsTaxExemption() {
		Item item = aMedicine();
		Rate taxAmount = taxRateCalculator.calculateTaxRate(item);
		assertThat(taxAmount).isEqualTo(ZERO_RATE);
	}
	
	private Item aBook() {
		return someItem()
				.withDescription("Book")
				.withCategory(ItemCategory.BOOK)
				.build();
	}
	
	private Item aFood() {
		return someItem()
				.withDescription("Food")
				.withCategory(ItemCategory.FOOD)
				.build();
	}
	
	private Item aMedicine() {
		return someItem()
				.withDescription("Medicine")
				.withCategory(ItemCategory.MEDICINE)
				.build();
	}

}
