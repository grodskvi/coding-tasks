package tasks.transferservice.validation;

import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

public class Preconditions {

    public static <T> void checkNotNull(T object, String message, Object... messageArgs) {
        check(object, Objects::nonNull, message, messageArgs);
    }

    public static <T> void check(T object, Predicate<T> validator, String message, Object... messageArgs) {
        if (!validator.test(object)) {
            throw new IllegalArgumentException(format(message, messageArgs));
        }
    }
}
