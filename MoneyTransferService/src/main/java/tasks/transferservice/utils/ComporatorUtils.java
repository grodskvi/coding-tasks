package tasks.transferservice.utils;

import static java.lang.String.format;

public class ComporatorUtils {

    public static <T extends Comparable<T>> T maxOf(T left, T right) {
        if (right == null || left == null) {
            throw new IllegalArgumentException(format("Can not compare '%s' with '%s'", left, right));
        }
        return left.compareTo(right) > 0 ? left : right;
    }

}
