package taxcalculator.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import taxcalculator.domain.Item;
import taxcalculator.domain.finance.Money;
import taxcalculator.dto.ErrorResponse;
import taxcalculator.dto.ItemDTO;
import taxcalculator.dto.Response;
import taxcalculator.dto.SalesTaxResponse;
import taxcalculator.exception.AccumulatedTranslationException;
import taxcalculator.service.ItemTranslatorService;
import taxcalculator.service.TaxCalculatorService;

@RestController
public class TaxCalculatorController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TaxCalculatorController.class);
	
	@Autowired
	private TaxCalculatorService taxCalculatorService;
	
	@Autowired
	private ItemTranslatorService itemTranslatorService;
	
    @RequestMapping(path="/taxcalculator", method={RequestMethod.POST})
    public ResponseEntity<? extends Response> taxcalculator(@RequestBody List<ItemDTO> items) {
    	try {
			List<Item> translatedItems = itemTranslatorService.translateToItems(items);
			Money salesTax = taxCalculatorService.calculateTaxAmount(translatedItems);
			
			return response(new SalesTaxResponse(salesTax), HttpStatus.OK);
		} catch (AccumulatedTranslationException e) {
			LOG.info("Error occured on attempt to handle items", e);
			return errorResponse(e, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.info("Error occured while handling items {}", items, e);
			return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageConversionException e) {
    	LOG.info("Can not parse request into json", e);
    	return errorResponse(e, HttpStatus.BAD_REQUEST);
    }
    
    private <T extends Response> ResponseEntity<T> response(T response, HttpStatus status) {
    	return new ResponseEntity<T>(response, status);
    }
    
    private ResponseEntity<ErrorResponse> errorResponse(Exception e, HttpStatus status) {
    	ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getClass().getCanonicalName());
    	return response(errorResponse, status);
    }    
}
