package taxcalulator.prototypes;

import taxcalulator.domain.ItemCategory;
import taxcalulator.domain.Item.ItemBuilder;

public class ItemPrototypes {

	public static ItemBuilder someItem() {
		return ItemBuilder.anItem()
				.withDescription("Some item")
				.withPrice("100")
				.withQuantity(1)
				.withCategory(ItemCategory.OTHER);
	}
}
