package util;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import model.Relation;

public final class Validate {
    private Validate() {}

    public static <A, B> boolean codomain(Set<A> domain, Set<B> codomain, Function<A, B> function) {
        return domain.stream()
            .allMatch(x -> codomain.contains(function.apply(x)));
    }

    public static <A, B> boolean codomain(Set<B> codomain, Map<A, B> map) {
        return map.values()
            .stream()
            .allMatch(codomain::contains);
    }

    public static <A, B> boolean mapping(Relation<A, B> relation) {
        for (A x : relation.domain()) {
            long count = relation.relationSet()
                .stream()
                .filter(pair -> pair.a().equals(x))
                .count();
            
            if (count != 1) {
                return false;
            }
        }

        return true;
    }

    public static <A> boolean subset(Set<A> subset, Set<A> set) {
        return subset.stream()
            .allMatch(set::contains);
    }

    public static <A> boolean matrix(int[][] entries) {
        if (entries.length < 1) {
            return false;
        }

        int columns = entries[0].length;

        for (int i = 1; i < entries.length; i++) {
            if (entries[i].length != columns) {
                return false;
            }
        }

        return true;
    }
}
