package taxcalculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import taxcalculator.domain.Item;
import taxcalculator.domain.finance.Money;
import taxcalculator.domain.finance.Rate;
import taxcalculator.domain.finance.Rates;

@Service
public class TaxCalculatorService {
	
	private List<TaxRateCalculator> rateCalculators;
	private BigDecimal precision = new BigDecimal("0.05");
	
	public TaxCalculatorService(List<TaxRateCalculator> rateCalculators) {
		this.rateCalculators = rateCalculators;
	}

	public Money calculateTaxAmount(List<Item> items) {
		if(items == null || items.isEmpty()) {
			return Money.ZERO_AMOUNT;
		}
		Money totalTaxAmount = Money.ZERO_AMOUNT;
		for(Item item: items) {
			if(item != null) {
				Money taxAmount = calculateItemTaxAmount(item);
				totalTaxAmount = totalTaxAmount.sum(taxAmount);
			}
		}
		
		return round(totalTaxAmount);
	}

	//TODO: move it somewhere
	private Money round(Money money) {
		BigDecimal amount = money.getAmount();
		BigDecimal scale = BigDecimal.ONE.divide(precision);
		BigDecimal roundedAmount = amount.multiply(scale).setScale(0, RoundingMode.UP).divide(scale);
		return new Money(roundedAmount);
	}

	private Money calculateItemTaxAmount(Item item) {
		Rate totalTaxRate = Rates.ZERO_RATE;
		for(TaxRateCalculator taxRateCalculator: rateCalculators) {
			Rate taxRate = taxRateCalculator.calculateTaxRate(item);
			totalTaxRate = totalTaxRate.sum(taxRate);
		}
		Money itemCost = item.getFaceValue();
		return itemCost.applyRate(totalTaxRate);
	}

}
