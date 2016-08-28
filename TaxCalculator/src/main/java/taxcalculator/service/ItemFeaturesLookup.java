package taxcalculator.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import taxcalculator.domain.ItemFeature;
import taxcalculator.dto.ItemDTO;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class ItemFeaturesLookup {
	
	private Map<String, ItemFeature> classifier;
	
	public ItemFeaturesLookup() {
		this(new HashMap<String, ItemFeature>() {{
			put("imported", ItemFeature.IMPORTED);
		}});
	}
	
	public ItemFeaturesLookup(Map<String, ItemFeature> classifier) {
		this.classifier = classifier;
	}
	
	public Set<ItemFeature> lookupItemFeatures(ItemDTO itemDTO) {
		Preconditions.checkNotNull(itemDTO, "Item can not be null");
		if(Strings.isNullOrEmpty(itemDTO.getDescription())) {
			return Collections.emptySet();
		}
		
		Set<ItemFeature> itemFeatures = new HashSet<ItemFeature>();
		String itemDescription = itemDTO.getDescription().toLowerCase();
		for(String itemName: classifier.keySet()) {
			if(itemDescription.contains(itemName)) {
				itemFeatures.add(classifier.get(itemName));
			}
		}
		return itemFeatures;
	}

}
