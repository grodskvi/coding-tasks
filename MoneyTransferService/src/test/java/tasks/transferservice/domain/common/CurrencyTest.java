package tasks.transferservice.domain.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tasks.transferservice.domain.common.Currency.EUR;
import static tasks.transferservice.domain.common.Currency.currencyOf;

public class CurrencyTest {

    @Test
    public void resolvesPredefinedCurrency() {
        assertThat(currencyOf("EUR")).isEqualTo(EUR);
    }

    @Test
    public void resolvesUnpredefinedCurrency() {
        final Currency GBP = currencyOf("GBP");
        assertThat(GBP.getIsoCode()).isEqualTo("GBP");
    }

    @Test
    public void resolvesUnnormalizedCurrencyCode() {
        assertThat(currencyOf("eur")).isEqualTo(EUR);
    }

    @Test
    public void failsToCreateCurrencyWithUndefinedIsoCode() {
        assertThatThrownBy(() -> currencyOf(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided value 'null' is not 3-symbol currency iso code");
    }

    @Test
    public void failsToCreateCurrencyWithShorterIsoCode() {
        assertThatThrownBy(() -> currencyOf("GB"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided value 'GB' is not 3-symbol currency iso code");
    }

    @Test
    public void failsToCreateCurrencyWithLongerIsoCode() {
        assertThatThrownBy(() -> currencyOf("GBPP"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided value 'GBPP' is not 3-symbol currency iso code");
    }

    @Test
    public void failsToCreateCurrencyWithWrongFormatIsoCode() {
        assertThatThrownBy(() -> currencyOf("G P"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided value 'G P' is not 3-symbol currency iso code");
    }

}