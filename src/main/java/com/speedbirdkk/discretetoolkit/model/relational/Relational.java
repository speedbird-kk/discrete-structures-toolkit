package com.speedbirdkk.discretetoolkit.model.relational;

import java.util.Set;

import com.speedbirdkk.discretetoolkit.model.Pair;

public sealed interface Relational<A, B> permits Mapping, Ordering, Relation {
    Set<A> domain();
    Set<B> codomain();
    Set<Pair<A, B>> relationSet();
}
