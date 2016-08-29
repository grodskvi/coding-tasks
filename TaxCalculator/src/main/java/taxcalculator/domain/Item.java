package taxcalculator.domain;

import static taxcalculator.domain.finance.Money.money;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import taxcalculator.domain.finance.Money;

import com.google.common.base.Preconditions;

public class Item {
	
	private String name;
	private Money price;
	private Quantity quantity;
	private ItemCategory category;
	private Set<ItemFeature> features;
	
	private Money totalCost;
	
	
	public Item(String name, Money price, Quantity quantity, ItemCategory category,
			Set<ItemFeature> features) {
		Preconditions.checkNotNull(name, "Name should be set to create item");
		Preconditions.checkNotNull(price, "Price should be set to create item");
		Preconditions.checkNotNull(quantity, "Quantity should be set to create item");
		
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
		this.features = features;
		
		totalCost = calculateTotalCost(price, quantity);
	}
	
	public ItemCategory getCategory() {
		return category;
	}
	
	public Money getTotalCost() {
		return totalCost;
	}
	
	public boolean isImported() {
		return features.contains(ItemFeature.IMPORTED);
	}
	
	private Money calculateTotalCost(Money price, Quantity quantity) {
		BigDecimal aPrice = price.getAmount();
		BigDecimal aQuantity = BigDecimal.valueOf(quantity.getQuantity());
		BigDecimal totalCost = aPrice.multiply(aQuantity);
		return new Money(totalCost);
	}
	
	@Override
	public String toString() {
		return "Item [name=" + name + ", price=" + price + ", quantity="
				+ quantity + ", category=" + category + ", features="
				+ features + "]";
	}



	public static class ItemBuilder {
		private String name;
		private Money price;
		private Quantity quantity = new Quantity(1);
		private ItemCategory category;
		private Set<ItemFeature> features = new HashSet<ItemFeature>();
		
		public ItemBuilder withName(String name) {
			this.name = name;
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
		public ItemBuilder withQuantity(Quantity quantity) {
			this.quantity = quantity;
			return this;
		}
		public ItemBuilder withQuantity(int quantity) {
			this.quantity = new Quantity(quantity);
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
		
		public ItemBuilder withFeatures(Set<ItemFeature> features) {
			this.features.addAll(features);
			return this;
		}
		
		public static ItemBuilder anItem() {
			return new ItemBuilder();
		}
		
		public Item build() {
			return new Item(name, price, quantity, category, features);
		}
		
		
		
	}
	
	
}
