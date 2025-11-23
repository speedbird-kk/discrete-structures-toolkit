package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.Matrix;
import model.Relational;

public final class RelationUtilities {
    private RelationUtilities() {}

    public static <A> Set<Pair<A, A>> reflexiveClosure(Set<Pair<A, A>> relationSet) {
        Set<Pair<A, A>> out = Set.copyOf(relationSet);

        relationSet.stream()
            .flatMap(p -> Stream.of(p.a(), p.b()))
            .forEach(x -> out.add(new Pair<>(x, x)));
        
        return out;
    }

    public static <A, B> Matrix adjacencyMatrix(Relational<A, B> relational) {
        List<A> domain = relational.domain().stream()
            .sorted(Comparator.comparing(Object::toString))
            .toList();

        List<B> codomain = relational.codomain().stream()
            .sorted(Comparator.comparing(Object::toString))
            .toList();

        Set<Pair<A, B>> relationSet = relational.relationSet();

        int[][] entries = new int[codomain.size()][domain.size()];

        for (int i = 0; i < codomain.size(); i++) {
            for (int j = 0; j < domain.size(); j++) {
                if (relationSet.contains(new Pair<A,B>(domain.get(j), codomain.get(i)))) {
                    entries[i][j] = 1;
                } else {
                    entries[i][j] = 0;
                }
            }
        }

        return new Matrix(entries, labelsFromList(codomain), labelsFromList(domain));
    }

    public static <A> List<String> labelsFromList(List<A> list) {
        return list.stream()
            .map(Object::toString)
            .toList();
    }

    public static <A> List<String> labelsFromSet(Set<A> set) {
        return set.stream()
            .map(Object::toString)
            .toList();
    }
}
