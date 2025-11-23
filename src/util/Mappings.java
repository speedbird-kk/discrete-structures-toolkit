package util;

import java.util.Set;

import model.Mapping;

public final class Mappings {
    private Mappings() {}

    public static <A> Mapping<A, A> identity(Set<A> domain) {
        return new Mapping<>(domain, x -> x);
    }

    public static <A, B> Mapping<A, B> constant(Set<A> domain, B constant) {
        return new Mapping<>(domain, x -> constant);
    }

    public static <A, B> Mapping<Pair<A, B>, A> projectLeft(Set<Pair<A, B>> domain) {
        return new Mapping<>(domain, Pair::a);
    }

    public static <A, B> Mapping<Pair<A, B>, B> projectRight(Set<Pair<A, B>> domain) {
        return new Mapping<>(domain, Pair::b);
    }

    public static <A, B>  Mapping<Pair<A, B>, Pair<B, A>> swap(Set<Pair<A, B>> domain) {
        return new Mapping<>(domain, p -> new Pair<>(p.b(), p.a()));
    }
}
