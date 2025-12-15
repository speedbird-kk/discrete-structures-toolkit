package com.speedbirdkk.discretetoolkit.utils.graph;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.speedbirdkk.discretetoolkit.model.graph.UEdge;
import com.speedbirdkk.discretetoolkit.model.graph.UGraph;
import com.speedbirdkk.discretetoolkit.utils.Validate;

public final class UGraphs {
    private UGraphs() {}

    /**
     * Constructs an induced subgraph of a graph {@code g} with the set of {@code vertices}.
     * 
     * <p>More formally, constructs an undirected graph with the set of vertices as {@code vertices}
     * and the set of edges as all edges in {@code g} with both endpoints of the edge contained
     * in {@code vertices}.
     * @param <A> the vertex type
     * @param g the graph from which to construct an induced subgraph
     * @param vertices the set of vertices in the induced subgraph
     * @throws NullPointerException if {@code g} is null
     * @throws NullPointerException if {@code vertices} is null
     * @throws IllegalArgumentException if {@code vertices} is not a subset of the set of vertices
     * in {@code g}
     * @return the induced subgraph of {@code g} with the set of {@code vertices}
     */
    public static <A> UGraph<A> inducedSubgraph(UGraph<A> g, Set<A> vertices) {
        Objects.requireNonNull(g, "Undirected graph must not be null");
        Objects.requireNonNull(vertices, "Set of vertices must not be null");

        if (!Validate.subset(vertices, g.vertices())) {
            throw new IllegalArgumentException(
            "Set of vertices of the induced subgraph must be a subset of the set of vertices of the graph");
        }

        Set<UEdge<A>> edges = g.edges().stream()
            .filter(e -> vertices.contains(e.u()) && vertices.contains(e.v()))
            .collect(Collectors.toSet());

        return new UGraph<>(vertices, edges);
    }
}
