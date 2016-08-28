package taxcalculator.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.mockito.Mockito.*;
import taxcalculator.domain.Item;
import taxcalculator.domain.finance.Money;
import taxcalculator.domain.finance.Rate;
import taxcalculator.prototypes.ItemPrototypes;
import taxcalculator.service.TaxCalculatorService;
import taxcalculator.service.TaxRateCalculator;
import static taxcalculator.domain.finance.Rate.aRateOf;

public class TaxCalculatorServiceTest {
	
	private TaxCalculatorService service;

	@Test
	public void returnsZeroAmountForNullItems() {
		service = new TaxCalculatorService(newArrayList());
		assertThat(service.calculateTaxAmount(null)).isEqualTo(Money.ZERO_AMOUNT);
	}
	
	@Test
	public void returnsZeroAmountForNoneItems() {
		service = new TaxCalculatorService(newArrayList());
		assertThat(service.calculateTaxAmount(newArrayList())).isEqualTo(Money.ZERO_AMOUNT);
	}
	
	@Test
	public void ignoresNullItems() {
		service = new TaxCalculatorService(newArrayList());
		assertThat(service.calculateTaxAmount(newArrayList((Item)null))).isEqualTo(Money.ZERO_AMOUNT);
	}
	
	@Test
	public void calculatesTaxAmountForSingleItem() {
		service = createTaxCalculatorService(aRateOf("0.1"));		
		Item item = item("50.5", 1);
		
		assertThat(service.calculateTaxAmount(newArrayList(item))).isEqualTo(Money.money("5.05"));
	}
	
	@Test
	public void combinesTaxRatesToCalculateTaxAmountForSingleItem() {
		service = createTaxCalculatorService(aRateOf("0.1"), aRateOf("0.07"));		
		Item item = item("100", 1);
		
		assertThat(service.calculateTaxAmount(newArrayList(item))).isEqualTo(Money.money("17"));
	}
	
	@Test
	public void calculatesTaxAmountForSingleItemInSeveralQuantities() {
		service = createTaxCalculatorService(aRateOf("0.1"), aRateOf("0.07"));		
		Item item = item("100", 2);
		
		assertThat(service.calculateTaxAmount(newArrayList(item))).isEqualTo(Money.money("34"));
	}
	
	@Test
	public void calculatesTotalTaxAmountorSeveralItems() {
		service = createTaxCalculatorService(Rate.aRateOf("0.1"));
		
		Item item = item("30", 1);
		Item anotherItem = item("20", 2);
		
		assertThat(service.calculateTaxAmount(newArrayList(item, anotherItem))).isEqualTo(Money.money("7"));	
	}
	
	@Test
	public void roundsTaxAmountWithPrecision05() {
		service = createTaxCalculatorService(Rate.aRateOf("0.1"));
		
		Item item = item("12.36", 1);
		assertThat(service.calculateTaxAmount(newArrayList(item))).isEqualTo(Money.money("1.25"));	

	}
	
	private TaxCalculatorService createTaxCalculatorService(Rate...rates) {
		List<TaxRateCalculator> taxRateCalculators = new ArrayList<TaxRateCalculator>();
		for(Rate rate: rates) {
			TaxRateCalculator taxRateCalculator = mock(TaxRateCalculator.class);
			when(taxRateCalculator.calculateTaxRate(any(Item.class))).thenReturn(rate);
			taxRateCalculators.add(taxRateCalculator);
		}
		
		return new TaxCalculatorService(taxRateCalculators);
	}
	
	private Item item(String price, int quantity) {
		return ItemPrototypes.someItem()
				.withPrice(price)
				.withQuantity(quantity)
				.build();
	}
	
	

}
