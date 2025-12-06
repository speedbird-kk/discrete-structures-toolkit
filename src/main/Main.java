package main;

import java.util.List;
import java.util.Set;

import model.graph.UEdge;
import model.graph.UGraph;
import model.relational.Relation;
import util.Relations;
import util.Sets;

public class Main {
    public static void main(String[] args) {
        UGraph<Integer> g = new UGraph<>(Sets.naturals(5), Set.of(
            new UEdge<>(0, 1),
            new UEdge<>(2, 3),
            new UEdge<>(1, 4),
            new UEdge<>(2, 4)
        ));

        System.out.println(g);
        System.out.println(g.degreeSequence());
        System.out.println(g.addEdge(new UEdge<Integer>(2, 1)));
        System.out.println(g.neighbours(3));
        System.out.println("\n\n");

        UGraph<Integer> h = UGraph.complete(Sets.integers(1, 4));
        UGraph<Integer> r = UGraph.path(List.of(1, 2, 3, 4, 5));

        System.out.println("h\n" + h);
        System.out.println("r\n" + r);

        System.out.println(h.degreeSequence());
        System.out.println(r.degreeSequence());
        System.out.println(h.neighbours(3));
    }
}
