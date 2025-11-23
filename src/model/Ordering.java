package model;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.Pair;
import util.RelationUtilities;

public final class Ordering<A> implements Relational<A, A> {
    private Set<A> domain;
    private Set<Pair<A, A>> coveringRelation;
    private Set<Pair<A, A>> relationSet;
    private Optional<Comparator<A>> comparator;

    public Ordering(Map<A, Set<A>> hasse) {
        domain = Set.copyOf(hasse.entrySet()
            .stream()
            .flatMap(e -> Stream.concat(Stream.of(e.getKey()), e.getValue().stream()))
            .collect(Collectors.toSet())
        );

        coveringRelation = Set.copyOf(hasse.entrySet()
            .stream()
            .flatMap(e -> e.getValue().stream()
                .map(value -> new Pair<>(e.getKey(), value)))
            .collect(Collectors.toSet())
        );

        // Transitive closure to be added
        relationSet = Set.copyOf(RelationUtilities.reflexiveClosure(coveringRelation));

        comparator = Optional.empty();
    }

    public Set<A> domain() {
        return domain;
    }

    public Set<A> codomain() {
        return domain;
    }

    public Set<Pair<A, A>> relationSet() {
        return relationSet;
    }
}
