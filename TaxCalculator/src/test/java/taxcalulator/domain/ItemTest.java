package taxcalulator.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static taxcalulator.domain.finance.Money.money;
import static taxcalulator.prototypes.ItemPrototypes.someItem;

import org.junit.Test;

import taxcalulator.domain.Item.ItemBuilder;
import taxcalulator.domain.finance.Money;

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
	public void calculatesFaceValue() {
		Item item = someItem()
				.withPrice("10.3")
				.withQuantity(3)
				.build();
		
		assertThat(item.getFaceValue()).isEqualTo(money("30.9"));
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
