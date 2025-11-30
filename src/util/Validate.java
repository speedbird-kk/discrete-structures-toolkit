package util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import model.Matrix;
import model.Relation;

public final class Validate {
    private Validate() {}

    public static <A> boolean reflexivity(Set<A> domain, Set<Pair<A, A>> relationSet) {
        return domain.stream()
            .allMatch(x -> relationSet.contains(new Pair<>(x, x)));
    }

    public static <A> boolean symmetry(Set<Pair<A, A>> relationSet) {
        return relationSet.stream()
            .allMatch(r -> relationSet.contains(new Pair<>(r.b(), r.a())));
    }

    public static <A> boolean antisymmetry(Set<Pair<A, A>> relationSet) {
        return relationSet.stream()
            .filter(r -> !r.a().equals(r.b()))
            .noneMatch(r -> relationSet.contains(new Pair<>(r.b(), r.a())));
    }

    public static <A> boolean transitivity(Set<Pair<A, A>> relationSet) {
        return relationSet.stream()
            .allMatch(r ->
                relationSet.stream()
                    .filter(s -> r.b().equals(s.a()))
                    .allMatch(s -> relationSet.contains(new Pair<>(r.a(), s.b())))
            );
    }

    public static <A> boolean ordering(Set<A> domain, Set<Pair<A, A>> relationSet) {
        return reflexivity(domain, relationSet)
            && antisymmetry(relationSet)
            && transitivity(relationSet);
    }

    public static <A> boolean reflexivity(Relation<A, A> relation) {
        return reflexivity(relation.domain(), relation.relationSet());
    }

    public static <A> boolean symmetry(Relation<A, A> relation) {
        return symmetry(relation.relationSet());
    }

    public static <A> boolean antisymmetry(Relation<A, A> relation) {
        return antisymmetry(relation.relationSet());
    }

    public static <A> boolean transitivity(Relation<A, A> relation) {
        return transitivity(relation.relationSet());
    }

    public static <A> boolean ordering(Relation<A, A> relation) {
        return ordering(relation.domain(), relation.relationSet());
    }

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

    public static <A> boolean comparator(List<A> sorted, Comparator<A> comparator) {
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (comparator.compare(sorted.get(i), sorted.get(i + 1)) > 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean matrix(int[][] entries) {
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

    public static boolean squareMatrix(Matrix matrix) {
        return matrix.rows() == matrix.columns();
    }
}
