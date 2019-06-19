package task.searchengine.utils;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class CollectionUtils {

    public static <T> Set<T> hashSet(T... elements) {
        return new HashSet<>(asList(elements));
    }
}
