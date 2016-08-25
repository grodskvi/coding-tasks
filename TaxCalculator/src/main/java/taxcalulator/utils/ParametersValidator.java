package taxcalulator.utils;

import java.math.BigDecimal;
import java.util.function.Predicate;

import taxcalulator.exception.InvalidDataException;

import com.google.common.base.Preconditions;

public class ParametersValidator {
	
	public static final Predicate<BigDecimal> NON_NEGATIVE_NUMBER = number -> number.signum() >= 0;
	
	public static <T> void validateParameter(T parameter, Predicate<T> validationPredicate, String failMessage) {
		Preconditions.checkNotNull(parameter, "Parameter is null");
		if(!validationPredicate.test(parameter)) {
			throw new InvalidDataException(failMessage);
		}
	}

}
