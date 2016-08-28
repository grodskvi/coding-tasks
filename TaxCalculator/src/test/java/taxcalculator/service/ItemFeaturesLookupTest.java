package taxcalculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import taxcalculator.domain.ItemFeature;
import taxcalculator.dto.ItemDTO;

public class ItemFeaturesLookupTest {
	
	private ItemFeaturesLookup itemFeaturesLookup;
	
	@Before
	public void setUp() {
		itemFeaturesLookup = new ItemFeaturesLookup();
	}

	@Test
	public void throwsInvalidDataExceptionIfItemIsNull() {
		assertThatThrownBy(() -> itemFeaturesLookup.lookupItemFeatures(null))
			.hasMessage("Item can not be null");
	}	
	
	@Test
	public void determinesThatItemHasNoFeaturesIfDescriptionIsNotProvided() {
		ItemDTO item = item(null);
		assertThat(itemFeaturesLookup.lookupItemFeatures(item)).isEmpty();	
	}
	
	@Test
	public void determinesThatItemHasImportedFeatureIfDescriptionContainsImportedKeyword() {
		ItemDTO item = item("Imported Chocolate Bars");
		assertThat(itemFeaturesLookup.lookupItemFeatures(item)).containsExactly(ItemFeature.IMPORTED);
	}
	
	@Test
	public void determinesThatItemHasNoFeatureIfDescriptionDoesNotContainKeywords() {
		ItemDTO item = item("Chocolate Bars");
		assertThat(itemFeaturesLookup.lookupItemFeatures(item)).isEmpty();
	}
	
	private ItemDTO item(String description) {
		ItemDTO item = new ItemDTO();
		item.setDescription(description);
		return item;
	}

}
