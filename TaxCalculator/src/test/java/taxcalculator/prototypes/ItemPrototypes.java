package taxcalculator.prototypes;

import taxcalculator.domain.ItemCategory;
import taxcalculator.domain.Item.ItemBuilder;

public class ItemPrototypes {

	public static ItemBuilder someItem() {
		return ItemBuilder.anItem()
				.withName("Some item")
				.withPrice("100")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER);
	}
}
