package util;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Matrix;
import model.Relational;

public final class RelationUtilities {
    private RelationUtilities() {}

    public static <A> Set<Pair<A, A>> reflexiveClosure(Set<Pair<A, A>> relationSet, Set<A> domain) {
        Set<Pair<A, A>> out = new HashSet<>(relationSet);

        for (A x : domain) {
            out.add(new Pair<>(x, x));
        }
        
        return out;
    }

    public static <A> Set<Pair<A, A>> transitiveClosure(Set<Pair<A, A>> relationSet, Set<A> domain) {
        Matrix adj = adjacencyMatrix(relationSet, domain, domain);
        Matrix adjClosed = MatrixUtilities.floydWarshall(adj);

        return relationSetFromMatrix(adjClosed, domain, domain);
    }

    public static <A, B> Set<Pair<A, B>> relationSetFromMatrix(Matrix adj, Set<A> domain, Set<B> codomain) {
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

    public static <A, B> Matrix adjacencyMatrix(Set<Pair<A, B>> relationSet, Set<A> domain, Set<B> codomain) {
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
        return adjacencyMatrix(relational.relationSet(), relational.domain(), relational.codomain());
    }
}
