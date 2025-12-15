package com.speedbirdkk.discretetoolkit.utils;

import java.util.*;

import com.speedbirdkk.discretetoolkit.model.Pair;

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