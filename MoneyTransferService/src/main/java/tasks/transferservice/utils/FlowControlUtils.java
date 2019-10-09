package tasks.transferservice.utils;

import static java.lang.String.format;

import java.util.Objects;
import java.util.function.BiPredicate;

public class FlowControlUtils {

    public static <T> T findFirstNotEqualTo(T sample, T left, T right) {
        return findFirstMatch(sample, (s, i) -> !Objects.equals(s, i), left, right);
    }

    public static <T, S> T findFirstMatch(S sample, BiPredicate<S, T> criteria, T left, T right) {
        if (criteria.test(sample, left)) {
            return left;
        }
        if (criteria.test(sample, right)) {
            return right;
        }
        throw new IllegalArgumentException(format("None of the items ('%s', '%s') matches criteria", left, right));
    }
}
