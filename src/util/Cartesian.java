package util;

import java.util.*;

public final class Cartesian {
    private Cartesian() {}

    public static <A, B> Set<Pair<A, B>> product(Set<A> a, Set<B> b) {
        Set<Pair<A, B>> out = new HashSet<>();

        for (A x : a) {
            for (B y : b) {
                out.add(new Pair<>(x, y));
            }
        }

        return out;
    }
}