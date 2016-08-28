package taxcalculator.service;

import java.util.HashMap;
import java.util.Map;

import taxcalculator.domain.ItemCategory;
import taxcalculator.dto.ItemDTO;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class ItemCategoryLookup {
	
	private Map<String, ItemCategory> classifier;

	public ItemCategoryLookup() {
		this(new HashMap<String, ItemCategory>() {{
			put("book", ItemCategory.BOOK);
			put("chocolate", ItemCategory.FOOD);
			put("pills", ItemCategory.MEDICINE);
			put("music cd", ItemCategory.OTHER);
		}});
	}
	
	public ItemCategoryLookup(Map<String, ItemCategory> classifier) {
		this.classifier = classifier;
	}

	public ItemCategory lookupItemCategory(ItemDTO itemDTO) {
		Preconditions.checkNotNull(itemDTO, "Item can not be null");
		if(Strings.isNullOrEmpty(itemDTO.getDescription())) {
			return ItemCategory.OTHER;
		}
		
		String itemDescription = itemDTO.getDescription().toLowerCase();
		for(String itemName: classifier.keySet()) {
			if(itemDescription.contains(itemName)) {
				return classifier.get(itemName);
			}
		}
		return ItemCategory.OTHER;
	}
}
