package tasks.transferservice.domain.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import tasks.transferservice.validation.Preconditions;

import java.util.function.Predicate;

@EqualsAndHashCode
@ToString
public class Currency {

    private static final Predicate<String> ISO_CODE_VALIDATOR = ((Predicate<String>) StringUtils::isAlpha)
            .and(isoCode -> isoCode.length() == 3);

    public static final Currency EUR = currencyOf("EUR");
    public static final Currency USD = currencyOf("USD");

    private final String isoCode;

    private Currency(String isoCode) {
        this.isoCode = isoCode;
    }

    String getIsoCode() {
        return isoCode;
    }

    public static String isoCodeOf(Currency currency) {
        return currency != null ? currency.getIsoCode() : null;
    }

    public static Currency currencyOf(String isoCode) {
        Preconditions.check(isoCode, ISO_CODE_VALIDATOR, "Provided value '%s' is not 3-symbol currency iso code", isoCode);
        return new Currency(isoCode.toUpperCase());
    }
}
