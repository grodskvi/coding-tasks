package taxcalculator.service;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import taxcalculator.domain.ItemCategory;
import taxcalculator.dto.ItemDTO;
import taxcalculator.exception.InvalidDataException;

public class ItemCategoryLookupTest {
	
	private ItemCategoryLookup itemCategoryLookup;
	
	@Before
	public void setUp() {
		Map<String, ItemCategory> classifier = new HashMap<String, ItemCategory>(){{
			put("chocolate", ItemCategory.FOOD);
		}};
		itemCategoryLookup = new ItemCategoryLookup();
	}

	@Test
	public void throwsInvalidDataExceptionIfItemIsNull() {
		assertThatThrownBy(() -> itemCategoryLookup.lookupItemCategory(null))
			.hasMessage("Item can not be null");
	}	
	
	@Test
	public void determinesItemCategoryAsOtherIfDescriptionIsNotProvided() {
		ItemDTO item = item(null);
		assertThat(itemCategoryLookup.lookupItemCategory(item)).isEqualTo(ItemCategory.OTHER);		
	}
	
	@Test
	public void determineItemCategoryIfDescriptionContainsNameFromClassifier() {
		ItemDTO item = item("Imported Chocolate Bars");
		assertThat(itemCategoryLookup.lookupItemCategory(item)).isEqualTo(ItemCategory.FOOD);
	}
	
	@Test
	public void determineItemCategoryAsOtherIfDescriptionDoesNotContainsNameFromClassifier() {
		ItemDTO item = item("Car");
		assertThat(itemCategoryLookup.lookupItemCategory(item)).isEqualTo(ItemCategory.OTHER);
	}
	
	private ItemDTO item(String description) {
		ItemDTO item = new ItemDTO();
		item.setDescription(description);
		return item;
	}

}
