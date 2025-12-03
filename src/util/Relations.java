package util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import model.Matrix;
import model.Pair;
import model.relational.Relation;
import model.relational.Relational;

public final class Relations {
    private Relations() {}

    public static <A> Set<Pair<A, A>> reflexiveClosure(Set<A> domain, Set<Pair<A, A>> relationSet) {
        Set<Pair<A, A>> out = new HashSet<>(relationSet);

        for (A x : domain) {
            out.add(new Pair<>(x, x));
        }
        
        return out;
    }

    public static <A> Set<Pair<A, A>> transitiveClosure(Set<A> domain, Set<Pair<A, A>> relationSet) {
        Matrix adj = adjacencyMatrix(domain, domain, relationSet);
        Matrix adjClosed = Matrices.floydWarshall(adj);

        return relationSetFromMatrix(domain, domain, adjClosed);
    }

    public static <A, B> Relation<B, A> inverse(Relational<A, B> relational) {
        Set<Pair<B, A>> inverseRelationSet = relational.relationSet().stream()
            .map(r -> new Pair<>(r.b(), r.a()))
            .collect(Collectors.toSet());
        
        return new Relation<>(relational.codomain(), relational.domain(), inverseRelationSet);
    }

    public static <A, B> Set<Pair<A, B>> relationSetFromMatrix(Set<A> domain, Set<B> codomain, Matrix adj) {
        Set<Pair<A, B>> out = new HashSet<>();
        int[][] entries = adj.entries();
        List<A> domainList = Transform.toSortedListFromSet(domain);
        List<B> codomainList = Transform.toSortedListFromSet(codomain);

        for (int i = 0; i < adj.rows(); i++) {
            for (int j = 0; j < adj.columns(); j++) {
                if (entries[i][j] == 1) {
                    out.add(new Pair<>(domainList.get(i), codomainList.get(j)));
                }
            }
        }

        return out;
    }

    public static <A, B> Matrix adjacencyMatrix(Set<A> domain, Set<B> codomain, Set<Pair<A, B>> relationSet) {
        List<A> domainList = Transform.toSortedListFromSet(domain);
        List<B> codomainList = Transform.toSortedListFromSet(codomain);
        
        int m = domainList.size();
        int n = codomainList.size();

        int[][] entries = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (relationSet.contains(new Pair<A,B>(domainList.get(i), codomainList.get(j)))) {
                    entries[i][j] = 1;
                } else {
                    entries[i][j] = 0;
                }
            }
        }

        return new Matrix(
            entries, Transform.toLabelsFromList(domainList), Transform.toLabelsFromList(codomainList));
    }

    public static <A, B> Matrix adjacencyMatrix(Relational<A, B> relational) {
        return adjacencyMatrix(relational.domain(), relational.codomain(), relational.relationSet());
    }
}
