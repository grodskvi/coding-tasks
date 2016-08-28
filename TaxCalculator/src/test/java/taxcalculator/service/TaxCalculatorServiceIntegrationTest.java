package taxcalculator.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static taxcalculator.domain.Item.ItemBuilder.anItem;
import static taxcalculator.domain.finance.Money.money;

import org.junit.Before;
import org.junit.Test;

import taxcalculator.domain.Item;
import taxcalculator.domain.ItemCategory;
import taxcalculator.domain.ItemFeature;
import taxcalculator.domain.finance.Money;

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
				.withName("Book")
				.withPrice("12.49")
				.withQuantity(1)
				.withCategory(ItemCategory.BOOK)
				.build();
		
		Item chocolateBar = anItem()
				.withName("Chocolate Bar")
				.withPrice("0.85")
				.withQuantity(1)
				.withCategory(ItemCategory.FOOD)
				.build();
		
		Item musicCD = anItem()
				.withName("Music CD")
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
				.withName("Box of Chocolates")
				.withPrice("10")
				.withQuantity(1)
				.withCategory(ItemCategory.FOOD)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Item perfume = anItem()
				.withName("Perfume")
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
				.withName("Box of Chocolates")
				.withPrice("11.25")
				.withQuantity(1)
				.withCategory(ItemCategory.FOOD)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Item importedPerfume = anItem()
				.withName("Perfume")
				.withPrice("27.99")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER)
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		Item perfume = anItem()
				.withName("Perfume")
				.withPrice("18.99")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER)
				.build();
		
		Item headachePills = anItem()
				.withName("Headache Pills")
				.withPrice("9.75")
				.withQuantity(1)
				.withCategory(ItemCategory.MEDICINE)
				.build();
		
		Money taxAmount = taxCalculatorService.calculateTaxAmount(newArrayList(chocolate, perfume, importedPerfume, headachePills));
		assertThat(taxAmount).isEqualTo(money("6.7"));
	}
	
	

}
