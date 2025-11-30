package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import exceptions.InvalidChooseException;

public final class Sets {
    private Sets() {}

    public static Set<Integer> integers(int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive)
            .boxed()
            .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<Integer> naturals(int endExclusive) {
        return integers(0, endExclusive);
    }

    public static <A> Set<A> singleton(A element) {
        return Set.of(element);
    }

    public static Set<Boolean> booleans() {
        return Set.of(true, false);
    }

    public static <A, B> Set<Pair<A, B>> cartesian(Set<A> A, Set<B> B) {
        return Cartesian.product(A, B);
    }

    public static Set<Pair<Integer, Integer>> integerPairs(int n, int m) {
        return Cartesian.product(naturals(n), naturals(m));
    }

    public static <A> Set<Pair<A, A>> identityRelationPairs(Set<A> A) {
        return A.stream()
            .map(e -> new Pair<>(e, e))
            .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<Pair<Integer, Integer>> integerIdentityRelationPairs(int n) {
        return identityRelationPairs(naturals(n));
    }

    public static Set<Character> lowercaseAlphabet() {
        return IntStream.rangeClosed('a', 'z')
            .mapToObj(c -> (char) c)
            .collect(Collectors.toUnmodifiableSet());
    }

    public static <E extends Enum<E>> Set<E> enumValues(Class<E> e) {
        return Collections.unmodifiableSet(EnumSet.allOf(e));
    }

    public static <A> Set<Set<A>> powerSet(Set<A> set) {
        Set<Set<A>> powerSet = new HashSet<>();
        List<A> setList = new ArrayList<>(set);
        int setSize = set.size();
        
        for (int i = 0; i < (1 << setSize); i++) {
            Set<A> subset = new HashSet<>();
            for (int j = 0; j < setSize; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(setList.get(j));
                }
            }

            powerSet.add(subset);
        }

        return powerSet;
    }

    public static <A> Set<Set<A>> choose(Set<A> set, int k) {
        if (k < 0 || k > set.size()) {
            throw new InvalidChooseException("k must be between 0 and size of the set");
        }

        List<A> list = new ArrayList<>(set);
        Set<Set<A>> result = new HashSet<>();

        chooseRecursive(list, 0, k, new ArrayList<>(), result);

        return Set.copyOf(result);
    }

    private static <A> void chooseRecursive(
        List<A> list, int index, int remaining, List<A> current, Set<Set<A>> result
    ) {
        if (remaining == 0) {
            result.add(Set.copyOf(current));
            return;
        }

        if (index == list.size()) {
            return;
        }

        current.add(list.get(index));
        chooseRecursive(list, index + 1, remaining - 1, current, result);
        current.remove(current.size() - 1);

        chooseRecursive(list, index + 1, remaining, current, result);
    }

    public static <A> Set<A> union(Set<A> A, Set<A> B) {
        return Stream.concat(A.stream(), B.stream())
            .collect(Collectors.toUnmodifiableSet());
    }

    public static <A> Set<A> intersection(Set<A> A, Set<A> B) {
        return A.stream()
            .filter(B::contains)
            .collect(Collectors.toUnmodifiableSet());
    }

    public static <A> Set<A> difference(Set<A> A, Set<A> B) {
        return A.stream()
            .filter(a -> !B.contains(a))
            .collect(Collectors.toUnmodifiableSet());
    }
}
