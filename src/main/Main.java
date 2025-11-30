package main;

import java.util.Set;

import model.Relation;
import util.RelationUtilities;
import util.Sets;

public class Main {
    public static void main(String[] args) {
        Set<Integer> domain = Set.of(1, 2, 3, 4);
        Set<Integer> codomain = Set.of(2, 4, 5, 6);
        Relation<Integer, Integer> r = new Relation<>(domain, codomain, (x, y) -> x + 1 == y);

        System.out.println(r);
        System.out.println("\nadjacency matrix:\n");
        System.out.println(RelationUtilities.adjacencyMatrix(r));

        Set<Integer> s = Sets.integers(1, 4);
        System.out.println(Sets.choose(s, 2));
    }
}
