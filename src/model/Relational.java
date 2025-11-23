package model;

import java.util.Set;

import util.Pair;

public sealed interface Relational<A, B> permits Mapping, Ordering, Relation {
    Set<A> domain();
    Set<B> codomain();
    Set<Pair<A, B>> relationSet();
}
