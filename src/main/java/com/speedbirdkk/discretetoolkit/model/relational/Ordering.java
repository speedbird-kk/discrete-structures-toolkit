package com.speedbirdkk.discretetoolkit.model.relational;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.speedbirdkk.discretetoolkit.exceptions.InvalidComparatorException;
import com.speedbirdkk.discretetoolkit.model.Pair;
import com.speedbirdkk.discretetoolkit.utils.Relations;
import com.speedbirdkk.discretetoolkit.utils.Validate;

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

        relationSet = Set.copyOf(
            Relations.reflexiveClosure(
                domain, Relations.transitiveClosure(domain, coveringRelation)
            )
        );

        comparator = Optional.empty();
    }

    public Ordering(Set<A> domain, Comparator<A> comparator) {
        this.domain = Set.copyOf(domain);
        this.comparator = Optional.of(comparator);

        List<A> sorted = new ArrayList<>(domain);
        sorted.sort(comparator);

        if (!Validate.comparator(sorted, comparator)) {
            throw new InvalidComparatorException("Comparator must be consistent with a linear ordering");
        }

        Set<Pair<A, A>> covers = new HashSet<>();

        for (int i = 0; i < sorted.size() - 1; i++) {
            covers.add(new Pair<>(sorted.get(i), sorted.get(i + 1)));
        }

        coveringRelation = Set.copyOf(covers);

        relationSet = Relations.reflexiveClosure(
            this.domain, Relations.transitiveClosure(this.domain, coveringRelation));
    }

    public Ordering(Set<Pair<A, A>> relationSet) {
        // validate relationSet is an ordering
        // infer domain
        // construct coveringRelation

        // make another constructor where domain is specified too
    }

    public static <A> Ordering<A> fromHasse(Map<A, Set<A>> hasse) {
        return new Ordering<>(hasse);
    }

    public static <A> Ordering<A> fromComparator(Set<A> domain, Comparator<A> comparator) {
        return new Ordering<>(domain, comparator);
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
