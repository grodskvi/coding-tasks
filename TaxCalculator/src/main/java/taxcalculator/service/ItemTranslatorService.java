package taxcalculator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import taxcalculator.domain.Item;
import taxcalculator.domain.ItemCategory;
import taxcalculator.domain.ItemFeature;
import taxcalculator.domain.finance.Money;
import static taxcalculator.domain.Item.ItemBuilder.anItem;
import taxcalculator.dto.ItemDTO;
import taxcalculator.exception.AccumulatedTranslationException;
import taxcalculator.exception.DomainObjectTranslationException;

public class ItemTranslatorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ItemTranslatorService.class);
	
	private ItemCategoryLookup itemCategoryLookup;
	private ItemFeaturesLookup itemFeaturesLookup;
	
	public ItemTranslatorService(ItemCategoryLookup itemCategoryLookup,
			ItemFeaturesLookup itemFeaturesLookup) {
		this.itemCategoryLookup = itemCategoryLookup;
		this.itemFeaturesLookup = itemFeaturesLookup;
	}

	public Item translateToItem(ItemDTO itemDTO) throws DomainObjectTranslationException {
		LOG.debug("Converting {} to item", itemDTO);
		try {
			return doTranslateToItem(itemDTO);
		} catch (Exception e) {
			LOG.info("Error occured while converting {} to item", itemDTO, e);
			String message = String.format("Failed to create item for '%d %s at %s'",  itemDTO.getCount(), itemDTO.getDescription(), itemDTO.getUnitPrice());
			throw new DomainObjectTranslationException(message, e);
		}
	}
	
	public List<Item> translateToItems(List<ItemDTO> itemDTOs) throws AccumulatedTranslationException {
		List<Item> items = new ArrayList<Item>();
		AccumulatedTranslationException accumulatedTranslationException = new AccumulatedTranslationException();
		for(ItemDTO itemDTO: itemDTOs) {
			try {
				items.add(translateToItem(itemDTO));
			} catch (DomainObjectTranslationException e) {
				accumulatedTranslationException.addException(e);
			}
		}
		if(accumulatedTranslationException.hasErrors()) {
			throw accumulatedTranslationException;
		}
		return items;
	}
	
	private Item doTranslateToItem(ItemDTO itemDTO) {
		ItemCategory itemCategory = itemCategoryLookup.lookupItemCategory(itemDTO);
		Set<ItemFeature> itemFeatures = itemFeaturesLookup.lookupItemFeatures(itemDTO);
		
		return anItem()
				.withName(itemDTO.getDescription())
				.withPrice(new Money(itemDTO.getUnitPrice()))
				.withQuantity(itemDTO.getCount())
				.withCategory(itemCategory)
				.withFeatures(itemFeatures)
				.build();
	}

}
