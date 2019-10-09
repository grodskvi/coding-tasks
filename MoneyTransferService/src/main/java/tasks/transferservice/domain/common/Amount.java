package tasks.transferservice.domain.common;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.ToString;
import tasks.transferservice.validation.Preconditions;

@ToString
@Getter
public class Amount {

    private static final String PRECONDITION_ERROR_MESSAGE = "Can not create amount with value '%s'";
    private static Predicate<BigDecimal> POSITIVE_AMOUNT_CHECK = amount ->
            amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;


    public static final Amount ZERO_AMOUNT = amountOf(BigDecimal.ZERO);

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        this.value = value;
    }

    public Amount increaseBy(Amount amount) {
        Preconditions.checkNotNull(amount, "Attempted to increase '%s' amount with null", value);
        return amountOf(this.value.add(amount.value));
    }

    public Amount decreaseBy(Amount amount) {
        Preconditions.checkNotNull(amount, "Attempted to decrease '%s' amount with null", value);
        Preconditions.check(this, amount::isLessThenOrEquals, "Attempted to decrease '%s' by '%s'", value, amount.value);

        return amountOf(this.value.subtract(amount.value));
    }

    public boolean isLessThen(Amount amount) {
        return value.compareTo(amount.value) < 0;
    }

    public boolean isLessThenOrEquals(Amount amount) {
        return value.compareTo(amount.value) <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Amount amount = (Amount) o;
        return value.compareTo(amount.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public static Amount amountOf(String amount) {
        Preconditions.checkNotNull(amount, PRECONDITION_ERROR_MESSAGE, amount);
        try {
            BigDecimal value = new BigDecimal(amount);
            return amountOf(value);
        } catch (NumberFormatException e) {
            String message = format(PRECONDITION_ERROR_MESSAGE, amount);
            throw new IllegalArgumentException(message);
        }
    }

    public static Amount amountOf(BigDecimal amount) {
        Preconditions.check(amount, POSITIVE_AMOUNT_CHECK, PRECONDITION_ERROR_MESSAGE, amount);

        return new Amount(amount);
    }
}
