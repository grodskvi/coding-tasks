package taxcalculator.domain;

import static taxcalculator.domain.finance.Money.money;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import taxcalculator.domain.finance.Money;

import com.google.common.base.Preconditions;

public class Item {
	
	private String description;
	private Money price;
	private ItemQuantity quantity;
	private ItemCategory category;
	private Set<ItemFeature> features;
	
	private Money faceValue;
	
	
	public Item(String description, Money price, ItemQuantity quantity, ItemCategory category,
			Set<ItemFeature> features) {
		Preconditions.checkNotNull(price, "Price should be set to create item");
		Preconditions.checkNotNull(quantity, "Quantity should be set to create item");
		
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
		this.features = features;
		
		faceValue = calculateFaceValue(price, quantity);
	}
	
	public ItemCategory getCategory() {
		return category;
	}
	
	public Money getFaceValue() {
		return faceValue;
	}
	
	public boolean isImported() {
		return features.contains(ItemFeature.IMPORTED);
	}
	
	private Money calculateFaceValue(Money price, ItemQuantity quantity) {
		BigDecimal aPrice = price.getAmount();
		BigDecimal aQuantity = BigDecimal.valueOf(quantity.getQuantity());
		BigDecimal faceValue = aPrice.multiply(aQuantity);
		return new Money(faceValue);
	}
	
	public static class ItemBuilder {
		private String description;
		private Money price;
		private ItemQuantity quantity = new ItemQuantity(1);
		private ItemCategory category;
		private Set<ItemFeature> features = new HashSet<ItemFeature>();
		
		public ItemBuilder withDescription(String description) {
			this.description = description;
			return this;
		}
		public ItemBuilder withPrice(Money price) {
			this.price = price;
			return this;
		}
		public ItemBuilder withPrice(String price) {
			this.price = money(price);
			return this;
		}
		public ItemBuilder withQuantity(ItemQuantity quantity) {
			this.quantity = quantity;
			return this;
		}
		public ItemBuilder withQuantity(int quantity) {
			this.quantity = new ItemQuantity(quantity);
			return this;
		}
		public ItemBuilder withCategory(ItemCategory category) {
			this.category = category;
			return this;
		}
		public ItemBuilder withFeature(ItemFeature feature) {
			features.add(feature);
			return this;
		}
		
		public static ItemBuilder anItem() {
			return new ItemBuilder();
		}
		
		public Item build() {
			return new Item(description, price, quantity, category, features);
		}
		
		
		
	}
	
	
}
