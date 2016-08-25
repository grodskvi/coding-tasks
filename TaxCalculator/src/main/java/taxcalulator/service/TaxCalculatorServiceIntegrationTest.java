package taxcalulator.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static taxcalulator.domain.Item.ItemBuilder.anItem;
import static taxcalulator.domain.finance.Money.money;

import org.junit.Before;
import org.junit.Test;

import taxcalulator.domain.Item;
import taxcalulator.domain.ItemCategory;
import taxcalulator.domain.ItemFeature;
import taxcalulator.domain.finance.Money;

public class TaxCalculatorServiceIntegrationTest {
	
	private TaxCalculatorService taxCalculatorService;
	
	@Before
	public void setUp() {
		TaxRateCalculator importDutyCalculator = (TaxRateCalculator)new ImportDutyCalculator();
		TaxRateCalculator defaultTaxRateCalculator = new DefaultTaxRateCalculator();
		taxCalculatorService = new TaxCalculatorService(newArrayList(importDutyCalculator, defaultTaxRateCalculator));
	}

	@Test
	public void calculatesTaxAmountForGroupOfIems() {
		Item book = anItem()
				.withDescription("Book")
				.withPrice("12.49")
				.withQuantity(1)
				.withCategory(ItemCategory.BOOK)
				.build();
		
		Item chocolateBar = anItem()
				.withDescription("Chocolate Bar")
				.withPrice("0.85")
				.withQuantity(1)
				.withCategory(ItemCategory.FOOD)
				.build();
		
		Item musicCD = anItem()
				.withDescription("Music CD")
				.withPrice("14.99")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER)
				.build();
		
		Money taxAmount = taxCalculatorService.calculateTaxAmount(newArrayList(book, chocolateBar, musicCD));
		assertThat(taxAmount).isEqualTo(money("1.5"));
	}
	
	@Test
	public void calculatesTaxAmountForGroupOfImportedIems() {
		
		Item chocolate = anItem()
				.withDescription("Box of Chocolates")
				.withPrice("10")
				.withQuantity(1)
				.withCategory(ItemCategory.FOOD)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Item perfume = anItem()
				.withDescription("Perfume")
				.withPrice("47.5")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Money taxAmount = taxCalculatorService.calculateTaxAmount(newArrayList(chocolate, perfume));
		assertThat(taxAmount).isEqualTo(money("7.65"));
	}
	
	@Test
	public void calculatesTaxAmountForGroupOfIemsWithSomeImported() {
		
		Item chocolate = anItem()
				.withDescription("Box of Chocolates")
				.withPrice("11.25")
				.withQuantity(1)
				.withCategory(ItemCategory.FOOD)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Item importedPerfume = anItem()
				.withDescription("Perfume")
				.withPrice("27.99")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Item perfume = anItem()
				.withDescription("Perfume")
				.withPrice("18.99")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER)
				.build();
		
		Item headachePills = anItem()
				.withDescription("Headache Pills")
				.withPrice("9.75")
				.withQuantity(1)
				.withCategory(ItemCategory.MEDICINE)
				.build();
		
		Money taxAmount = taxCalculatorService.calculateTaxAmount(newArrayList(chocolate, perfume, importedPerfume, headachePills));
		assertThat(taxAmount).isEqualTo(money("6.7"));
	}
	
	

}
