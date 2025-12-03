package model.relational;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import exceptions.InvalidCodomainException;
import exceptions.NotAMappingException;
import exceptions.NotASubsetException;
import model.Pair;
import model.Relation;
import model.Relational;
import util.Validate;

public final class Mapping<A, B> implements Relational<A, B> {
    private final Set<A> domain;
    private final Set<B> codomain;
    private final Function<A, B> function;
    private final Set<Pair<A, B>> relationSet;

    /**
     * Construct a mapping with a defined domain and function.
     * Codomain is inferred to be the image of the domain under the function.
     * The mapping is therefore surjective.
     */
    public Mapping(Set<A> domain, Function<A, B> function) {
        this.domain = Set.copyOf(domain);
        this.function = function;
        
        this.codomain = Set.copyOf(domain.stream()
            .map(function)
            .collect(Collectors.toSet()));
        
        this.relationSet = Set.copyOf(domain.stream()
            .map(x -> new Pair<>(x, function.apply(x)))
            .collect(Collectors.toSet()));
    }

    /**
     * Construct a mapping with a defined domain, codomain and function.
     * Throws InvalidCodomainException if codomain does not contain all elements mapped from domain.
     */
    public Mapping(Set<A> domain, Set<B> codomain, Function<A, B> function) {
        this.domain = Set.copyOf(domain);
        this.codomain = Set.copyOf(codomain);
        this.function = function;

        if (!Validate.codomain(domain, codomain, function)) {
            throw new InvalidCodomainException("Invalid codomain for specified domain and function");
        }

        relationSet = Set.copyOf(domain.stream()
            .map(x -> new Pair<>(x, function.apply(x)))
            .collect(Collectors.toSet()));
    }

    /**
     * Construct a mapping from a Relation object.
     * Throws NotAMappingException if the relation is not a mapping.
     */
    public Mapping(Relation<A, B> relation) {
        if (!Validate.mapping(relation)) {
            throw new NotAMappingException("Specified relation is not a mapping");
        }

        this.domain = relation.domain();
        this.codomain = relation.codomain();
        this.relationSet = relation.relationSet();
        
        this.function = x -> 
            relation.relationSet().stream()
                .filter(pair -> pair.a().equals(x))
                .findFirst()
                .map(Pair::b)
                .orElseThrow();
    }

    /**
     * Construct a mapping from a map object.
     * Codomain is inferred to be the image of the domain under the function.
     * The mapping is therefore surjective.
     */
    public Mapping(Map<A, B> map) {
        this.domain = Set.copyOf(map.keySet());
        this.codomain = Set.copyOf(map.values());
        this.function = x -> map.get(x);
        this.relationSet = Set.copyOf(map.entrySet()
            .stream()
            .map(e -> new Pair<>(e.getKey(), e.getValue()))
            .collect(Collectors.toSet()));
    }

    /**
     * Construct a mapping with a defined domain, codomain and map.
     * Throws InvalidCodomainException if codomain does not contain all values in the map.
     */
    public Mapping(Set<B> codomain, Map<A, B> map) {
        this.codomain = Set.copyOf(codomain);

        if (!Validate.codomain(codomain, map)) {
            throw new InvalidCodomainException("Invalid codomain for specified map");
        }

        this.domain = Set.copyOf(map.keySet());
        this.function = x -> map.get(x);
        this.relationSet = Set.copyOf(map.entrySet()
            .stream()
            .map(e -> new Pair<>(e.getKey(), e.getValue()))
            .collect(Collectors.toSet()));
    }

    public Set<A> domain() {
        return domain;
    }

    public Set<B> codomain() {
        return codomain;
    }

    public Function<A, B> function() {
        return function;
    }

    public Set<Pair<A, B>> relationSet() {
        return relationSet;
    }

    public boolean maps(A x, B y) {
        return relationSet.contains(new Pair<>(x, y));
    }

    public B imageOf(A x) {
        return function.apply(x);
    }

    public Set<B> imageOf(Set<A> subset) {
        if (!Validate.subset(subset, domain)) {
            throw new NotASubsetException("Cannot take image of a set that is not a subset of the domain");
        }

        return subset.stream()
            .map(function)
            .collect(Collectors.toSet());
    }

    /**
     * Composes this mapping f: A -> B with an after mapping g: B -> C
     * Returns a new mapping f ∘ g: A -> C
     * Throws InvalidCodomainException if codomain of this mapping is not a subset of the domain of the after mapping
     */
    public <C> Mapping<A, C> compose(Mapping<B, C> after) {
        if (!Validate.subset(codomain, after.domain())) {
            throw new InvalidCodomainException(
                "Codomain of first mapping must be a subset of the domain of the second mapping");
        }

        return new Mapping<A, C>(
            domain,
            after.codomain(),
            x -> after.function().apply(this.function.apply(x))
        );
    }

    public int size() {
        return relationSet.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mapping<?, ?>)) return false;

        Mapping<?, ?> other = (Mapping<?, ?>) o;

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
        return "Mapping(domain = " + domain
            + ", codomain = " + codomain
            + ", relation set = " + relationSet.toString()
            + ")";
    }
}
