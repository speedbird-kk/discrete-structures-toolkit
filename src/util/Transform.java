package util;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class Transform {
    private Transform() {}

    public static <A> List<A> toSortedListFromSet(Set<A> set) {
        return set.stream()
            .sorted(Comparator.comparing(Object::toString))
            .toList();
    }

    public static <A> List<String> toLabelsFromList(List<A> list) {
        return list.stream()
            .map(Object::toString)
            .toList();
    }

    public static <A> List<String> toLabelsFromSet(Set<A> set) {
        return set.stream()
            .map(Object::toString)
            .toList();
    }
}
