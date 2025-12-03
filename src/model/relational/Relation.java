package model.relational;

import java.util.function.*;
import java.util.stream.Collectors;

import model.Pair;
import model.Relational;
import util.Cartesian;

import java.util.*;

public final class Relation<A, B> implements Relational<A, B> {
    private final Set<A> domain;
    private final Set<B> codomain;
    private final BiPredicate<A, B> predicate;
    private final Set<Pair<A, B>> relationSet;

    /**
     * Construct a relation specifying:
     * - domain
     * - codomain
     * - bipredicate which defines the necessary and sufficient condition for two elements
     *   in the domain and codomain respectively to be related.
     */
    public Relation(Set<A> domain, Set<B> codomain, BiPredicate<A, B> predicate) {
        this.domain = Set.copyOf(domain);
        this.codomain = Set.copyOf(codomain);
        this.predicate = predicate;

        relationSet = Set.copyOf(Cartesian.product(domain, codomain)
            .stream()
            .filter(pair -> predicate.test(pair.a(), pair.b()))
            .collect(Collectors.toSet()));
    }

    /**
     * Construct a relation specifying the relation set.
     * Domain and codomain are the smallest sets given this relation set.
     */
    public Relation(Set<Pair<A, B>> relationSet) {
        this.relationSet = Set.copyOf(relationSet);

        this.domain = Set.copyOf(relationSet.stream()
            .map(Pair::a)
            .collect(Collectors.toSet()));
        
        this.codomain = Set.copyOf(relationSet.stream()
            .map(Pair::b)
            .collect(Collectors.toSet()));
        
        this.predicate = (a, b) -> relationSet.contains(new Pair<>(a, b));
    }

    /**
     * Construct a relation specifying the domain, codomain and the relation set.
     */
    public Relation(Set<A> domain, Set<B> codomain, Set<Pair<A, B>> relationSet) {
        this.domain = Set.copyOf(domain);
        this.codomain = Set.copyOf(codomain);
        this.relationSet = Set.copyOf(relationSet);

        this.predicate = (a, b) -> relationSet.contains(new Pair<>(a, b));
    }

    public Set<A> domain() {
        return domain;
    }

    public Set<B> codomain() {
        return codomain;
    }

    public Set<Pair<A,B>> relationSet() {
        return relationSet;
    }

    public BiPredicate<A, B> predicate() {
        return predicate;
    }

    public boolean relates(A a, B b) {
        return relationSet.contains(new Pair<>(a, b));
    }

    public int size() {
        return relationSet.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relation<?, ?>)) return false;

        Relation<?, ?> other = (Relation<?, ?>) o;

        return this.domain.equals(other.domain)
            && this.codomain.equals(other.codomain)
            && this.relationSet.equals(other.relationSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, codomain, relationSet);
    }

    @Override
    public String toString() {
        return "Relation = " + relationSet.toString();
    }
}