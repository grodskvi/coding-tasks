package taxcalculator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import taxcalculator.domain.Item;
import taxcalculator.domain.Precision;
import taxcalculator.domain.finance.Money;
import taxcalculator.domain.finance.Rate;
import taxcalculator.domain.finance.Rates;

import static taxcalculator.domain.Precision.precision;

@Service
public class TaxCalculatorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TaxCalculatorService.class);
	
	private List<TaxRateCalculator> rateCalculators;
	private Precision precision = precision("0.05");
	
	public TaxCalculatorService(List<TaxRateCalculator> rateCalculators) {
		this.rateCalculators = rateCalculators;
	}

	public Money calculateTaxAmount(List<Item> items) {
		if(items == null || items.isEmpty()) {
			LOG.debug("There is no items to calculate tax amount");
			return Money.ZERO_AMOUNT;
		}
		Money totalTaxAmount = Money.ZERO_AMOUNT;
		for(Item item: items) {
			if(item != null) {
				Money taxAmount = calculateItemTaxAmount(item);
				LOG.debug("Tax amount for item {} is {}", item, taxAmount);
				totalTaxAmount = totalTaxAmount.sum(taxAmount);
			}
		}
		
		Money roundedTaxAmount = new Money(totalTaxAmount, precision);
		LOG.debug("Total tax amount is {}, after rounding -- {}", totalTaxAmount, roundedTaxAmount);
		return roundedTaxAmount;
	}

	private Money calculateItemTaxAmount(Item item) {
		Rate totalTaxRate = Rates.ZERO_RATE;
		for(TaxRateCalculator taxRateCalculator: rateCalculators) {
			Rate taxRate = taxRateCalculator.calculateTaxRate(item);
			totalTaxRate = totalTaxRate.sum(taxRate);
		}
		LOG.debug("Total tax rate for item {} is {}", item, totalTaxRate);
		Money itemCost = item.getTotalCost();
		return itemCost.applyRate(totalTaxRate);
	}

}
