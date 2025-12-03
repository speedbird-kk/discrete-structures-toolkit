package model.relational;

import java.util.Set;

import model.Pair;

public sealed interface Relational<A, B> permits Mapping, Ordering, Relation {
    Set<A> domain();
    Set<B> codomain();
    Set<Pair<A, B>> relationSet();
}
