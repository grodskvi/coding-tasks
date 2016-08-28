package taxcalculator.service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static taxcalculator.domain.Item.ItemBuilder.anItem;
import static taxcalculator.domain.ItemCategory.BOOK;
import static taxcalculator.domain.ItemCategory.MEDICINE;
import static taxcalculator.domain.ItemCategory.OTHER;
import static taxcalculator.domain.ItemFeature.IMPORTED;
import static taxcalculator.domain.ItemQuantity.quantity;
import static taxcalculator.domain.finance.Money.money;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import taxcalculator.domain.Item;
import taxcalculator.dto.ItemDTO;
import taxcalculator.exception.AccumulatedTranslationException;
import taxcalculator.exception.DomainObjectTranslationException;


@RunWith(MockitoJUnitRunner.class)
public class ItemTranslatorServiceTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@InjectMocks
	private ItemTranslatorService itemTranslatorService;
	
	@Mock
	private ItemCategoryLookup itemCategoryLookup;
	
	@Mock
	private ItemFeaturesLookup itemFeaturesLookup;
	
	@Test
	public void createsItemBasedOnDTO() throws DomainObjectTranslationException {
		ItemDTO itemDTO = itemDTO("Item", "12.01", 2);
		Item item = itemTranslatorService.translateToItem(itemDTO);
		
		Item expectedItem = anItem()
								.withName("Item")
								.withPrice("12.01")
								.withQuantity(2)
								.build();
		
		assertThat(item).isEqualToComparingOnlyGivenFields(expectedItem, "name", "price", "quantity");
	}
	
	@Test
	public void enrichesItemWithCategory() throws DomainObjectTranslationException {
		ItemDTO itemDTO = itemDTO("Item", "12.01", 2);
		when(itemCategoryLookup.lookupItemCategory(eq(itemDTO))).thenReturn(BOOK);
		Item item = itemTranslatorService.translateToItem(itemDTO);
		
		assertThat(item.getCategory()).isEqualTo(BOOK);
	}
	
	@Test
	public void enrichesItemWithFeatures() throws DomainObjectTranslationException {
		ItemDTO itemDTO = itemDTO("Item", "12.01", 2);
		when(itemFeaturesLookup.lookupItemFeatures(eq(itemDTO))).thenReturn(newHashSet(IMPORTED));
		Item item = itemTranslatorService.translateToItem(itemDTO);
		
		assertThat(item.isImported()).isTrue();
	}
	
	@Test
	public void failsToConstructItemWithNegativeQuantity() {
		ItemDTO itemDTO = itemDTO("Item", "12.01", -1);
		
		assertThatThrownBy(() -> itemTranslatorService.translateToItem(itemDTO))
			.hasMessageStartingWith("Failed to create item")
			.isInstanceOf(DomainObjectTranslationException.class);
	}
	
	@Test
	public void failsToConstructItemWithNegativePrice() {
		ItemDTO itemDTO = itemDTO("Item", "-12.01", 1);
		
		assertThatThrownBy(() -> itemTranslatorService.translateToItem(itemDTO))
			.hasMessageStartingWith("Failed to create item")
			.isInstanceOf(DomainObjectTranslationException.class);
	}
	
	@Test
	public void translatesListOfItems() throws AccumulatedTranslationException {
		when(itemCategoryLookup.lookupItemCategory(any(ItemDTO.class))).thenReturn(BOOK, MEDICINE);
		when(itemFeaturesLookup.lookupItemFeatures(any(ItemDTO.class))).thenReturn(emptySet(), newHashSet(IMPORTED));
		
		ItemDTO book = itemDTO("Book", "5.57", 1);
		ItemDTO pills = itemDTO("Pills", "10.0", 2);
		
		assertThat(itemTranslatorService.translateToItems(newArrayList(book, pills)))
			.extracting("name", "price", "quantity", "category", "features")
			.containsExactly(
					tuple("Book", money("5.57"), quantity(1), BOOK, emptySet()),
					tuple("Pills", money("10.0"), quantity(2), MEDICINE, newHashSet(IMPORTED)));
	}
	
	@Test
	public void accumulatesTranslationErrors() {
		when(itemCategoryLookup.lookupItemCategory(any(ItemDTO.class))).thenReturn(BOOK, MEDICINE, OTHER);
		when(itemFeaturesLookup.lookupItemFeatures(any(ItemDTO.class))).thenReturn(emptySet(), newHashSet(IMPORTED), emptySet());
		
		ItemDTO book = itemDTO("Book", "5.57", -1);
		ItemDTO pills = itemDTO("Pills", "10.0", 2);
		ItemDTO musicCD = itemDTO("Music CD", "-47", 2);
		
		assertThatThrownBy(() -> itemTranslatorService.translateToItems(newArrayList(book, pills, musicCD)))
			.isInstanceOf(AccumulatedTranslationException.class)
			.hasMessageMatching("Failed to create item.*Book.*; Failed to create item.*Music CD.*");
	}
	
	private ItemDTO itemDTO(String description, String price, int quantity) {
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setDescription(description);
		itemDTO.setUnitPrice(new BigDecimal(price));
		itemDTO.setCount(quantity);
		
		return itemDTO;
	}

}
