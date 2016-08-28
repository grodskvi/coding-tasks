package taxcalculator.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static taxcalculator.domain.finance.Money.money;
import static taxcalculator.prototypes.ItemPrototypes.someItem;

import org.junit.Test;

import taxcalculator.domain.Item;
import taxcalculator.domain.ItemFeature;
import taxcalculator.domain.Item.ItemBuilder;
import taxcalculator.domain.finance.Money;

public class ItemTest {

	@Test
	public void failsToCreateItemWhenPriceIsNotSet() {
		ItemBuilder itemBuilder = someItem()
				.withPrice((Money)null);
		assertThatThrownBy(() -> itemBuilder.build())
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("Price should be set");
	}
	
	@Test
	public void failsToCreateItemWhenQuantityIsNotSet() {
		ItemBuilder itemBuilder = someItem()
				.withQuantity(null);
		assertThatThrownBy(() -> itemBuilder.build())
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("Quantity should be set");
	}
	
	@Test
	public void calculatesItemTotalCost() {
		Item item = someItem()
				.withPrice("10.3")
				.withQuantity(3)
				.build();
		
		assertThat(item.getTotalCost()).isEqualTo(money("30.9"));
	}
	
	@Test
	public void indicatesWhetherItemIsImported() {
		Item item = someItem()
				.withFeature(ItemFeature.IMPORTED)
				.build();
		
		assertThat(item.isImported()).isTrue();
	}
	
	@Test
	public void indicatesThatItemIsNotImportedIfItDoesNotHaveImpoprtedFeature() {
		Item item = someItem()
				.build();
		
		assertThat(item.isImported()).isFalse();
	}
}
