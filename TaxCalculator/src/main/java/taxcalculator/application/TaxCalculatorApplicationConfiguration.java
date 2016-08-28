package taxcalculator.application;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

import ch.qos.logback.classic.pattern.MessageConverter;
import taxcalculator.service.DefaultTaxRateCalculator;
import taxcalculator.service.ImportDutyCalculator;
import taxcalculator.service.ItemCategoryLookup;
import taxcalculator.service.ItemFeaturesLookup;
import taxcalculator.service.ItemTranslatorService;
import taxcalculator.service.TaxCalculatorService;
import taxcalculator.service.TaxRateCalculator;

@Configuration
@ComponentScan(basePackages="taxcalculator.rest")
public class TaxCalculatorApplicationConfiguration {
	
	@Bean
	public TaxCalculatorService taxCalculatorService() {
		TaxRateCalculator defaultTaxCalculatorService = new DefaultTaxRateCalculator();
		TaxRateCalculator importDutyRateCalculator = new ImportDutyCalculator();
		return new TaxCalculatorService(newArrayList(defaultTaxCalculatorService, importDutyRateCalculator));
	}
	
	@Bean
	public ItemTranslatorService itemTranslatorService() {
		ItemCategoryLookup itemCategoryLookup = new ItemCategoryLookup();
		ItemFeaturesLookup itemFeaturesLookup = new ItemFeaturesLookup();
		return new ItemTranslatorService(itemCategoryLookup, itemFeaturesLookup);
	}

}
